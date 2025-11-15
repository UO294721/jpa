package uo.ri.gui.mechanic;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;

/**
 * Panel to display list of all mechanics in a table
 */
public class MechanicsListPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8166563497647420491L;
	private JTable mechanicsTable;
	private DefaultTableModel tableModel;
	private MechanicCrudService service;

	public MechanicsListPanel() {
		this.service = Factories.service.forMechanicCrudService();
		initializeUI();
		loadMechanics();
	}

	private void initializeUI() {
		setLayout(new BorderLayout(10, 10));

		// Title
		JLabel titleLabel = new JLabel("Mechanics List");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(titleLabel, BorderLayout.NORTH);

		// Table
		String[] columnNames = { "ID", "NIF", "Name", "Surname" };
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Make table read-only
			}
		};

		mechanicsTable = new JTable(tableModel);
		mechanicsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mechanicsTable.setRowHeight(25);
		mechanicsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

		JScrollPane scrollPane = new JScrollPane(mechanicsTable);
		add(scrollPane, BorderLayout.CENTER);

		// Buttons panel
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

		JButton refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(e -> loadMechanics());
		buttonPanel.add(refreshButton);

		JButton exportButton = new JButton("Export to CSV");
		exportButton.addActionListener(e -> exportToCSV());
		buttonPanel.add(exportButton);

		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void loadMechanics() {
		try {
			// Clear existing data
			tableModel.setRowCount(0);

			// Load mechanics from service
			List<MechanicDto> mechanics = service.findAll();

			// Add to table
			for (MechanicDto mechanic : mechanics) {
				Object[] row = { mechanic.id, mechanic.nif, mechanic.name,
						mechanic.surname };
				tableModel.addRow(row);
			}

			// Update status
			JOptionPane.showMessageDialog(this,
					"Loaded " + mechanics.size() + " mechanics", "Success",
					JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this,
					"Error loading mechanics: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	private void exportToCSV() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Export Mechanics to CSV");
		fileChooser.setSelectedFile(new java.io.File("mechanics.csv"));

		int userSelection = fileChooser.showSaveDialog(this);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			java.io.File file = fileChooser.getSelectedFile();
			try (java.io.PrintWriter writer = new java.io.PrintWriter(file)) {
				// Write header
				writer.println("ID,NIF,Name,Surname");

				// Write data
				for (int i = 0; i < tableModel.getRowCount(); i++) {
					StringBuilder sb = new StringBuilder();
					for (int j = 0; j < tableModel.getColumnCount(); j++) {
						sb.append(tableModel.getValueAt(i, j));
						if (j < tableModel.getColumnCount() - 1) {
							sb.append(",");
						}
					}
					writer.println(sb.toString());
				}

				JOptionPane.showMessageDialog(this,
						"Exported successfully to: " + file.getAbsolutePath(),
						"Export Success", JOptionPane.INFORMATION_MESSAGE);

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this,
						"Error exporting: " + ex.getMessage(), "Export Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}