package presentation;

import application.PreparePetForAdoptionUseCase;
import application.RegisterRescuedPetUseCase;
import domain.pet.Pet;
import domain.pet.Species;
import infrastructure.H2PetRepository;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

public class PetAdoptionApp extends JFrame {
    private final H2PetRepository repository;
    private final RegisterRescuedPetUseCase registerUseCase;
    private final PreparePetForAdoptionUseCase prepareUseCase;
    private final DefaultTableModel tableModel;

    private final JTextField nameField = new JTextField();
    private final JTextField ageField = new JTextField();
    private final JComboBox<Species> speciesField = new JComboBox<>(Species.values());

    public PetAdoptionApp() {
        super("ONG de Adocao de Pets");
        this.repository = H2PetRepository.localFile();
        this.registerUseCase = new RegisterRescuedPetUseCase(repository);
        this.prepareUseCase = new PreparePetForAdoptionUseCase(repository);
        this.tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Especie", "Idade (meses)", "Status"}, 0);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 520);
        setLocationRelativeTo(null);
        buildLayout();
        refreshTable();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PetAdoptionApp().setVisible(true));
    }

    private void buildLayout() {
        JPanel form = new JPanel(new GridLayout(2, 4, 8, 8));
        form.add(new JLabel("Nome"));
        form.add(new JLabel("Especie"));
        form.add(new JLabel("Idade em meses"));
        form.add(new JLabel(""));
        form.add(nameField);
        form.add(speciesField);
        form.add(ageField);

        JButton registerButton = new JButton("Cadastrar resgate");
        registerButton.addActionListener(event -> registerPet());
        form.add(registerButton);

        JTable table = new JTable(tableModel);
        JButton prepareButton = new JButton("Marcar como apto para adocao");
        prepareButton.addActionListener(event -> prepareSelectedPet(table));

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(prepareButton, BorderLayout.SOUTH);
    }

    private void registerPet() {
        try {
            String name = nameField.getText();
            Species species = (Species) speciesField.getSelectedItem();
            int age = Integer.parseInt(ageField.getText());
            registerUseCase.execute(name, species, age);
            nameField.setText("");
            ageField.setText("");
            refreshTable();
        } catch (RuntimeException exception) {
            showError(exception.getMessage());
        }
    }

    private void prepareSelectedPet(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Selecione um pet na tabela");
            return;
        }
        try {
            String petId = tableModel.getValueAt(row, 0).toString();
            prepareUseCase.execute(petId);
            refreshTable();
        } catch (RuntimeException exception) {
            showError(exception.getMessage());
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Pet> pets = repository.findAll();
        for (Pet pet : pets) {
            tableModel.addRow(new Object[]{
                    pet.id().value(),
                    pet.name(),
                    pet.species(),
                    pet.ageInMonths(),
                    pet.status()
            });
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Atencao", JOptionPane.WARNING_MESSAGE);
    }
}
