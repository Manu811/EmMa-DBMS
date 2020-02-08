import java.io.*;
import javax.swing.*;
import java.awt.event.*;

public class DatabaseGUI extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;

	// Atributos
	private JMenuBar	mbPrincipal;
	private JMenu		menuBase, menuTabla;
	private JMenuItem	miBase, miTabla;
	private JMenuItem	miSalir;

    private JPanel		panel;

	private BaseGUI base;
	private TablaGUI tabla;

	private DatabaseAD dbad = new DatabaseAD();

	public void setBaseActual(String nombre){
		PrintWriter archivoOut;
		try{
			//1. Abrir el archivo
			archivoOut = new PrintWriter(new FileWriter("BaseActual.txt"));
			
			//2. Escribir o almacenar datos en el archivo
			archivoOut.println(nombre);
			
			//3. Cerrar el archivo
			archivoOut.close();
			
			System.out.println("Base actual: "+nombre);
		}
		catch(IOException ioe){
			System.out.println("Error: "+ioe);
		}
	}

	public DatabaseGUI(){
		super("EmMa");

		base = new BaseGUI(this,dbad);

		mbPrincipal			= new JMenuBar();
		menuBase			= new JMenu("Gestion de Bases de Datos");
		menuTabla			= new JMenu("Gestion de Tablas");
		miBase				= new JMenuItem("Gestion Base");
		miTabla				= new JMenuItem("Gestion Tabla");
		
		miSalir		= new JMenuItem("Salir");

        panel		= new JPanel();	

		miBase.addActionListener(this);
		miTabla.addActionListener(this);
		miSalir.addActionListener(this);

		menuBase.add(miBase);
		
		menuTabla.add(miTabla);

		mbPrincipal.add(menuBase);
		mbPrincipal.add(menuTabla);
		mbPrincipal.add(miSalir);

		menuTabla.setEnabled(false);

		setJMenuBar(mbPrincipal);
		setExtendedState(MAXIMIZED_BOTH); 
		//setUndecorated(true);				//Verdadera pantalla completa
		setVisible(true);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void activaBotones(){
		menuTabla.setEnabled(true);
	}

	public void inactivaBotones(){
		menuTabla.setEnabled(false);
	}

	public void actionPerformed(ActionEvent e){
		//String respuesta = "";

		if(e.getSource() == miBase){
			panel.setVisible(false);
			panel = base.getPanel2();
			panel.setVisible(true);
			add(panel);
			setVisible(true);
		}

		if(e.getSource() == miTabla){
			panel.setVisible(false);
			tabla = new TablaGUI(dbad);
			panel = tabla.getPanel2();
			panel.setVisible(true);
			add(panel);
			setVisible(true);
		}

		if(e.getSource() == miSalir){
			dbad.datosListaArchivo();
			System.exit(0);
		}
	}
	public static void main(String args[]){
        new DatabaseGUI();
    }
}