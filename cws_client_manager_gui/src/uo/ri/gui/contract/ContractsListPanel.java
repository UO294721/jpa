package uo.ri.gui.contract;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;

public class ContractsListPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTable contractsTable;
	private DefaultTableModel tableModel;
	private ContractCrudService service;

	public ContractsListPanel() {
		this.service = Factories.service.forContractCrudService();
		initializeUI();
		loadContracts();
	}

	private void initializeUI() {
		setLayout(new BorderLayout(10, 10));

		JLabel titleLabel = new JLabel("Contracts List");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(titleLabel, BorderLayout.NORTH);

		String[] columnNames = { "ID", "NIF", "Settlement", "Num Payrolls",
				"State" };
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		contractsTable = new JTable(tableModel);
		contractsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contractsTable.setRowHeight(25);
		contractsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

		JScrollPane scrollPane = new JScrollPane(contractsTable);
		add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		JButton refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(e -> loadContracts());
		buttonPanel.add(refreshButton);

		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void loadContracts() {
		try {
			tableModel.setRowCount(0);
			List<ContractSummaryDto> contracts = service.findAll();

			for (ContractSummaryDto contract : contracts) {
				Object[] row = { contract.id, contract.nif,
						String.format("%.2f", contract.settlement), contract.numPayrolls,
						contract.state };
				tableModel.addRow(row);
			}

			JOptionPane.showMessageDialog(this,
					"Loaded " + contracts.size() + " contracts", "Success",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this,
					"Error loading contracts: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}