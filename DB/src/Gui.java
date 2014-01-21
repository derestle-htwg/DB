import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;


public class Gui extends JFrame implements ActionListener, TableModel, ChangeListener, ListSelectionListener{

	private class cWohnung
	{
		public int PK;
		public String Name;
		public String Land;
	}
	
	private List<cWohnung> Data = new LinkedList<cWohnung>();
	
	private class cLand
	{
		public cLand(String inLand, int inPK)
		{
			Land = inLand;
			PK = inPK;
		}
		public String Land;
		public int PK;
		@Override
		public String toString()
		{
			return Land;
		}
	}
	
	private class cFeature
	{
		public cFeature(String inFeature, int inPK)
		{
			Feature = inFeature;
			PK = inPK;
		}
		public String Feature;
		public int PK;
		@Override
		public String toString()
		{
			return Feature;
		}
	}
	
	JLabel lblLand;
	JComboBox<cLand> cboCountry;
	
	JLabel lblSlider;
	JSlider sldMinimumRooms;
	
	JLabel lblDateStart;
	JTextField txtDateStart;
	
	JLabel lblDateEnd;
	JTextField txtDateEnd;
	
	JLabel lblFeatures;
	JList<cFeature> lstFeature;
	
	JButton btnSearch;
	JButton btnRent;
	
	JTable tblResults;
	//SQLAccess con = new SQLAccess();
	
	public Gui() {
		lblLand = new JLabel("Land:");
		cboCountry = new JComboBox<cLand>();
		
		lblSlider = new JLabel("Minimale Anzahl an Zimmer");
		JSlider sldMinimumRooms = new JSlider(0,15);
		
		lblDateStart = new JLabel("Startdatum");
		txtDateStart = new JTextField();
		txtDateStart.setText("dd.mm.yyyy");
		
		lblDateEnd = new JLabel("Enddatum");
		txtDateEnd = new JTextField();
		txtDateEnd.setText("dd.mm.yyyy");
		
		lblFeatures = new JLabel("Ausstattung");
		lstFeature = new JList<cFeature>();
		
		btnSearch = new JButton("Suche"); 
		btnSearch.addActionListener(this);
		
		tblResults = new JTable();
		btnRent = new JButton("Buchen");
		
		LoadLand();
		loadFeature();
		
		
		this.setLayout(null);
	
		this.add(lblLand);
		this.add(cboCountry);
		
		this.add(lblSlider);
		this.add(sldMinimumRooms);
		
		this.add(lblDateStart);
		this.add(txtDateStart);
		
		this.add(lblDateEnd);
		this.add(txtDateEnd);
		
		this.add(lblFeatures);
		this.add(lstFeature);
				
		this.add(tblResults);
		this.add(btnRent);
		
		
		lblLand.setLocation(0, 0);
		lblLand.setSize(100,30);
		cboCountry.setLocation(100, 0);
		cboCountry.setSize(180, 30);
		
		lblSlider.setLocation(0, 40);
		lblSlider.setSize(100,30);
		sldMinimumRooms.setLocation(100, 40);
		sldMinimumRooms.setSize(180, 30);
		
		lblDateStart.setLocation(0, 80);
		lblDateStart.setSize(100,30);
		txtDateStart.setLocation(100, 80);
		txtDateStart.setSize(180, 30);
		
		lblDateEnd.setLocation(0, 120);
		lblDateEnd.setSize(100,30);
		txtDateEnd.setLocation(100, 120);
		txtDateEnd.setSize(180, 30);
		
		lblFeatures.setLocation(0, 160);
		lblFeatures.setSize(100,30);
		lstFeature.setLocation(100, 160);
		lstFeature.setSize(180, 110);
		
		tblResults.setLocation(0, 280);
		tblResults.setSize(290,250);
		btnRent.setLocation(80, 550);
		btnRent.setSize(170, 30);

		tblResults.setModel(this);
		
		reloadResults();
		
		cboCountry.addActionListener(this);
		sldMinimumRooms.addChangeListener(this);
		txtDateStart.addActionListener(this);
		txtDateEnd.addActionListener(this);
		lstFeature.addListSelectionListener(this);
	}
	
	private void loadFeature()
	{
		cFeature[] nf = new cFeature[4];
		nf[0] = new cFeature("Sauna", 1);
		nf[1] = new cFeature("Garage", 2);
		nf[2] = new cFeature("Strom", 3);
		nf[3] = new cFeature("Wasser", 4);
		
		lstFeature.setListData(nf);
	}
	
	private void LoadLand()
	{
		/*java.sql.ResultSet res = con.getData("SELECT Name FROM tblLand");
		try {
			while(res.next()){
				cboCountry.addItem(res.getString("Name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		cLand country = new cLand("a",1);
		cboCountry.addItem(country);
		country = new cLand("b",2);
		cboCountry.addItem(country);
		country = new cLand("c",3);
		cboCountry.addItem(country);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Gui g = new Gui();
		g.setVisible(true);
		g.setSize(300, 650);
	}
	
	private void reloadResults()
	{
		//Data.clear();
		
		cWohnung nw = new cWohnung();
		nw.PK = Data.size()+1;
		nw.Name = "Name";
		nw.Land = "De";
		Data.add(nw);
		
		//tblResults.set
		notifyTbl();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnSearch)
		{
			this.removeAll();
			//SQL Load Lists
			
		}
		else
		{
			
		}
	}

	LinkedList<TableModelListener> modelListeners = new LinkedList<TableModelListener>();
	
	private void notifyTbl()
	{
		for(TableModelListener l : modelListeners)
		{
			l.tableChanged(null);
		}
	}
	
	@Override
	public void addTableModelListener(TableModelListener l) {
		modelListeners.add(l);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public int getColumnCount() {

		return 3;
	}

	String[] Columns = {"PK","Name","Land"};
	
	@Override
	public String getColumnName(int columnIndex) {
		return Columns[columnIndex];
	}

	@Override
	public int getRowCount() {

		return Data.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String retVal = null;
		
		cWohnung wohnung = Data.get(rowIndex);
		
		switch(columnIndex)
		{
		case 0:
			retVal = ""+wohnung.PK;
			break;
		case 1:
			retVal = wohnung.Name;
			break;
		case 2:
			retVal = wohnung.Land;
			break;
		}
		
		return retVal;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		int i = 0;
		int a = 1/i;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		int i = 0;
		int a = 1/i;
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		reloadResults();
		
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		reloadResults();
		
	}

}
