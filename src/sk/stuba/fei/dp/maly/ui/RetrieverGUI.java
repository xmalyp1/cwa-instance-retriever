package sk.stuba.fei.dp.maly.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.table.DefaultTableModel;

import org.dllearner.core.ComponentInitException;
import org.semanticweb.owlapi.model.OWLException;

import sk.stuba.fei.dp.maly.retriever.InstanceRetriever;
import sk.stuba.fei.dp.maly.ui.models.IndividualsDatatableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JToggleButton;

public class RetrieverGUI {

	private static final String[] DATATABLE_COLUMNS = {"Individual name","Direct class"};
	
	private JFrame frmCwaInstanceRetriever;
	private JFileChooser fileChooser;
	private InstanceRetriever instanceRetriever;
	private File selectedFile;
	private JTextField conceptField;
	private JTable instancesTable;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					RetrieverGUI window = new RetrieverGUI();
					window.frmCwaInstanceRetriever.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RetrieverGUI() {
		initialize();
		instanceRetriever = new InstanceRetriever();
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));
		fileChooser.addChoosableFileFilter(new OwlFileFilter());
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCwaInstanceRetriever = new JFrame();
		frmCwaInstanceRetriever.getContentPane().setLayout(null);
		frmCwaInstanceRetriever.setSize(751, 414);

		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setBounds(0, 0, 729, 358);
		frmCwaInstanceRetriever.getContentPane().add(tabbedPane);

		tabbedPane.addTab("Load Ontology", createLoadTab());

		final JPanel panelRetriever = new JPanel();
		tabbedPane.addTab("Instance Retriever", null, panelRetriever, null);
		panelRetriever.setLayout(null);
		
		JLabel lblConcept = new JLabel("Concept :");
		lblConcept.setBounds(15, 16, 69, 20);
		panelRetriever.add(lblConcept);
		
		conceptField = new JTextField();
		conceptField.setToolTipText("Pizza and not (hasTopping some FishTopping)");
		conceptField.setBounds(99, 13, 428, 26);
		panelRetriever.add(conceptField);
		conceptField.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 94, 694, 214);
		panelRetriever.add(scrollPane);

		instancesTable = new JTable();
		scrollPane.setViewportView(instancesTable);
		instancesTable.setModel(new DefaultTableModel(DATATABLE_COLUMNS, 0));
		instancesTable.setFont(new Font("Tahoma", Font.PLAIN, 17));
		
		JButton btnGetInstances = new JButton("Get instances");
		btnGetInstances.setBounds(15, 52, 162, 29);
		panelRetriever.add(btnGetInstances);
		
		JToggleButton btnCwa = new JToggleButton("CWA Mode");
		btnCwa.setBounds(546, 12, 163, 29);
		panelRetriever.add(btnCwa);
		btnGetInstances.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(instanceRetriever.getOntology() == null){
					JOptionPane.showMessageDialog(panelRetriever, "The ontology is not loaded.", "Error",
							JOptionPane.ERROR_MESSAGE);	
					return;
				}
				
				try {
					instanceRetriever.initializeReasoner();
				} catch (ComponentInitException e1) {
					JOptionPane.showMessageDialog(panelRetriever, "Failed to initialize the CWA Reasoner component.", "Error",
							JOptionPane.ERROR_MESSAGE);					
					e1.printStackTrace();
					return;
				}
				
				if(instanceRetriever.getCwaReasoner() == null){
					JOptionPane.showMessageDialog(panelRetriever, "The reasoner was not loaded.", "Error",
							JOptionPane.ERROR_MESSAGE);					
					return;
				}
				
				if(conceptField.getText().isEmpty())
					return;
				
				List<IndividualsDatatableModel> individuals = instanceRetriever.getIndividuals(conceptField.getText(),btnCwa.isSelected());
				instancesTable.setModel(new DefaultTableModel(DATATABLE_COLUMNS, 0));
				DefaultTableModel model = (DefaultTableModel) instancesTable.getModel();
				for(IndividualsDatatableModel individual:individuals){
					Object[] row ={individual.getNamedIndividual(),individual.getIndividualClass()};
					model.addRow(row);
				}
				instancesTable.setDefaultEditor(Object.class, null);
				instancesTable.setModel(model);;
				
			}
		});
		


	}

	private JPanel createLoadTab() {
		final JPanel loadPanel = new JPanel();
		loadPanel.setLayout(null);

		JLabel lblLoadOntology = new JLabel("Load Ontology");
		lblLoadOntology.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblLoadOntology.setBounds(15, 16, 180, 32);
		loadPanel.add(lblLoadOntology);

		JLabel lblSelectedFile = new JLabel("Selected file :");
		lblSelectedFile.setBounds(15, 55, 95, 20);
		loadPanel.add(lblSelectedFile);

		final JLabel loadedFileLabel = new JLabel("");
		loadedFileLabel.setBounds(126, 55, 267, 20);
		loadPanel.add(loadedFileLabel);

		final JLabel loadedInfoLabel = new JLabel("");
		loadedInfoLabel.setBounds(15, 133, 415, 175);
		loadPanel.add(loadedInfoLabel);

		final JButton btnLoadOntology = new JButton("Load Ontology");
		btnLoadOntology.setBounds(145, 88, 147, 29);
		btnLoadOntology.setEnabled(false);
		loadPanel.add(btnLoadOntology);
		btnLoadOntology.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (selectedFile != null) {
						
						if(!OwlFileFilter.isOwlFile(selectedFile)){
							selectedFile = null;
							JOptionPane.showMessageDialog(loadPanel, "The selected file is not an ontology ! The retriever support only owl files.", "Error",
									JOptionPane.ERROR_MESSAGE);						
							return;
						}
						instanceRetriever.createOntology(selectedFile);
						btnLoadOntology.setEnabled(false);
						loadedInfoLabel.setText("Loaded ontology : " + selectedFile.getName());
					} else {
						JOptionPane.showMessageDialog(loadPanel, "The file is not selected !", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (OWLException e1) {
					JOptionPane.showMessageDialog(loadPanel, e1.getLocalizedMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JButton btnSelectFile = new JButton("Select file");
		btnSelectFile.setBounds(15, 88, 115, 29);
		loadPanel.add(btnSelectFile);
		btnSelectFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(loadPanel);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					selectedFile = fileChooser.getSelectedFile();
					loadedFileLabel.setText(selectedFile.getAbsolutePath());
					btnLoadOntology.setEnabled(true);

				}
			}
		});

		return loadPanel;
	}
}
