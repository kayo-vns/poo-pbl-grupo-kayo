package infrastructure;

import application.PetRepository;
import domain.pet.MedicalRecord;
import domain.pet.Pet;
import domain.pet.PetId;
import domain.pet.PetStatus;
import domain.pet.Species;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class H2PetRepository implements PetRepository {
    private final String jdbcUrl;

    public H2PetRepository(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        createSchema();
    }

    public static H2PetRepository localFile() {
        return new H2PetRepository("jdbc:h2:file:./data/ong-pets");
    }

    @Override
    public void save(Pet pet) {
        String sql = """
                MERGE INTO pets KEY(id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pet.id().value());
            statement.setString(2, pet.name());
            statement.setString(3, pet.species().name());
            statement.setInt(4, pet.ageInMonths());
            statement.setBoolean(5, pet.medicalRecord().vaccinated());
            statement.setBoolean(6, pet.medicalRecord().neutered());
            statement.setBoolean(7, pet.medicalRecord().underTreatment());
            statement.setString(8, pet.status().name());
            statement.setString(9, pet.fosterHomeName());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Could not save pet", exception);
        }
    }

    @Override
    public Optional<Pet> findById(PetId id) {
        String sql = "SELECT * FROM pets WHERE id = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.value());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(map(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Could not find pet", exception);
        }
    }

    @Override
    public List<Pet> findAll() {
        String sql = "SELECT * FROM pets ORDER BY name";
        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            List<Pet> pets = new ArrayList<>();
            while (resultSet.next()) {
                pets.add(map(resultSet));
            }
            return pets;
        } catch (SQLException exception) {
            throw new IllegalStateException("Could not list pets", exception);
        }
    }

    private void createSchema() {
        String sql = """
                CREATE TABLE IF NOT EXISTS pets (
                    id VARCHAR(36) PRIMARY KEY,
                    name VARCHAR(120) NOT NULL,
                    species VARCHAR(20) NOT NULL,
                    age_months INT NOT NULL,
                    vaccinated BOOLEAN NOT NULL,
                    neutered BOOLEAN NOT NULL,
                    under_treatment BOOLEAN NOT NULL,
                    status VARCHAR(40) NOT NULL,
                    foster_home_name VARCHAR(120)
                )
                """;
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException exception) {
            throw new IllegalStateException("Could not create database schema", exception);
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, "sa", "");
    }

    private Pet map(ResultSet resultSet) throws SQLException {
        MedicalRecord medicalRecord = new MedicalRecord(
                resultSet.getBoolean("vaccinated"),
                resultSet.getBoolean("neutered"),
                resultSet.getBoolean("under_treatment")
        );
        return Pet.restore(
                PetId.from(resultSet.getString("id")),
                resultSet.getString("name"),
                Species.valueOf(resultSet.getString("species")),
                resultSet.getInt("age_months"),
                medicalRecord,
                PetStatus.valueOf(resultSet.getString("status")),
                resultSet.getString("foster_home_name")
        );
    }
}
