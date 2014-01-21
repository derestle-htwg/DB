import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

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
		public cWohnung(int inPK, String inName, String inLand, int inZimmer, int inQm){
			PK = inPK;
			Name = inName;
			Land = inLand;
			Zimmer = inZimmer;
			Qm = inQm;
		}
		public int PK;
		public String Name;
		public String Land;
		public int Zimmer;
		public int Qm;
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
	SQLAccess con = new SQLAccess();
	
	public Gui() {
		lblLand = new JLabel("Land:");
		cboCountry = new JComboBox<cLand>();
		
		lblSlider = new JLabel("Minimale Anzahl an Zimmer");
		sldMinimumRooms = new JSlider(0,15);
		sldMinimumRooms.setValue(2);
		
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
		btnRent.addActionListener(this);
	}
	
	private void loadFeature()
	{
		java.sql.ResultSet res = con.getData("SELECT ID,Beschreibung FROM dbsys25.Ausstattung");
		try {
			Vector<cFeature> liste = new Vector<Gui.cFeature>();
			while(res.next()){
				cFeature feature = new cFeature(res.getString("Beschreibung"),res.getInt("ID"));
				liste.add(feature);
			}
			lstFeature.setListData(liste);	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void LoadLand()
	{
		java.sql.ResultSet res = con.getData("SELECT ID,Name FROM dbsys25.Land");
		try {
			while(res.next()){
				cLand country = new cLand(res.getString("Name"),res.getInt("ID"));
				cboCountry.addItem(country);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Gui g = new Gui();
		g.setVisible(true);
		g.setSize(300, 650);
	}
	
	private void reloadResults()
	{
		Data.clear();
		String SQLStatement = "SELECT  dbsys25.Ferienwohnung.ID,Quadratmeter,dbsys25.Ferienwohnung.Name,Zimmer"+
" FROM    dbsys25.Ferienwohnung"+
" LEFT JOIN (SELECT * FROM dbsys25.Buchung WHERE Von < '" + txtDateStart.getText() + "' AND Von+Dauer > '" +txtDateEnd.getText() +"') Buchung"+
" ON      Ferienwohnung.ID = Buchung.ferienwohnung_id"+
" INNER JOIN dbsys25.ferienwohnung_ausstattung"+
" ON      Ferienwohnung.ID = ferienwohnung_ausstattung.Ferienwohnung_ID"+
" INNER JOIN dbsys25.Land"+
" ON      Land_ID = Land.ID"+
" INNER JOIN dbsys25.Ausstattung"+
" ON      ferienwohnung_ausstattung.Ausstattung_ID = Ausstattung.ID"+
" WHERE   Land.Name = '" + cboCountry.getItemAt(cboCountry.getSelectedIndex()).Land + "'"+
" AND     Buchung.ID IS NULL" +
" AND Ferienwohnung.Zimmer >= " + sldMinimumRooms.getValue();

for(Object f : lstFeature.getSelectedValues())
{
	SQLStatement += " AND     ausstattung.beschreibung = '" + f.toString() + "'"; 
}


		System.out.println(SQLStatement);
		try {
		java.sql.ResultSet res = con.getData(SQLStatement);
			if(res != null)
			{
			while(res.next()){
				cWohnung wohnung = new cWohnung(res.getInt("ID"), res.getString("Name"), cboCountry.getSelectedItem().toString(),res.getInt("Zimmer"),res.getInt("Quadratmeter"));
				Data.add(wohnung);
			}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block

		}
		

		notifyTbl();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnRent)
		{
			int Dauer = 0;
			
			long start = (txtDateStart.getText());
			long End = Date.parse(txtDateEnd.getText());
			
			Dauer = (int) (End-start)/(24*3599);
			
			String SQLStatement = "INSERT dbsys25.Buchung (Dauer,Von,Kunde_ID,Ferienwohnung_ID) VALUES(" + Dauer + ",'" + txtDateStart.getText() + "',1," + Data.get(tblResults.getSelectedColumn()).PK + ")";
			System.out.println(SQLStatement);
			con.execute(SQLStatement);
			
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

		return 5;
	}

	String[] Columns = {"PK","Name","Land","Zimmer","Quadratmeter"};
	
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
			
		case 3:
			retVal = ""+wohnung.Zimmer;
			break;
		case 4:
			retVal = ""+wohnung.Qm;
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
		lblSlider.setText(""+sldMinimumRooms.getValue());
		reloadResults();
		
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		reloadResults();
		
	}

}
