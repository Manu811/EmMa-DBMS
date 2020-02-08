import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BaseGUI extends JFrame implements ActionListener, FocusListener{
    private static final long serialVersionUID = 1L;

    // Atributos
    private JTextField  tfBase;
    private JButton		bCrear, bUsar, bEliminar, bConsultar;
    
    private JTextArea  taDatos;
    private JPanel     panel1, panel2;
    
    private DatabaseGUI dbgui;
    private DatabaseAD  dbad;

    // Constructor
    public BaseGUI(DatabaseGUI dbg, DatabaseAD db){
        // 1. Crear los objetos de los atributos\
        dbad = db;
        dbgui = dbg;
        tfBase      = new JTextField();
        bCrear      = new JButton("Crear Base");
        bUsar       = new JButton("Usar Base");
        bEliminar   = new JButton("Eliminar Base");
        bConsultar  = new JButton("Consultar Bases");

        // Adicionar addActionListener a lo JButtons
        tfBase.addFocusListener(this);
        bCrear.addActionListener(this);
        bUsar.addActionListener(this);
        bEliminar.addActionListener(this);
        bConsultar.addActionListener(this);
        
        bEliminar.setEnabled(false);

        taDatos = new JTextArea(11,40);
        
        panel1 = new JPanel();
        panel2 = new JPanel();
        
        // 2. Definir los Layouts de los JPanels
        panel1.setLayout(new GridLayout(3,2));
        panel2.setLayout(new FlowLayout());
        
        // 3. Colocar los objetos de los atributos en los JPanels correspondientes
        panel1.add(new JLabel("Base de Datos:"));
        panel1.add(tfBase);
        panel1.add(bCrear);
        panel1.add(bUsar);
        panel1.add(bEliminar);
        panel1.add(bConsultar);
        
        panel2.add(panel1);
        panel2.add(new JScrollPane(taDatos));
        
        add(panel2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JPanel getPanel2(){
        return this.panel2;
    }

    public void focusGained(FocusEvent e){
        if(e.getSource() == tfBase){
            bEliminar.setEnabled(false);
        }
    }

    public void focusLost(FocusEvent e){}

    public void actionPerformed(ActionEvent e){
        String datos="";
        String respuesta="";

        if(e.getSource() == bCrear){
            String name = tfBase.getText();
            name.trim();

            if(name.isEmpty()){
            	respuesta = "Llene el campo con un nombre para la base de datos...";
            }
			else{
				respuesta = dbad.crearBase(name);	
			}
            taDatos.setText(respuesta);
        }
        
        if(e.getSource() == bUsar){
            String base = tfBase.getText();
            base.trim();

            if(base.isEmpty()){
            	respuesta = "Seleccione una base de datos...";
            }
            else{
                respuesta = dbad.consultarNombreBase(base);
                if(respuesta.equals("LISTA_VACIA") || respuesta.equals("NOT_FOUND")){
                    respuesta = "La base de datos "+base+" no fue localizada";
                }
                else{
                    dbgui.setBaseActual(base);
                    respuesta = "Base de datos "+base+" en uso";
                    dbgui.activaBotones();
                    bEliminar.setEnabled(true);
                }
            }
            taDatos.setText(respuesta);
        }

        if(e.getSource() == bEliminar){
            String base = tfBase.getText();
            base.trim();

            if(base.isEmpty()){
            	respuesta = "Seleccione una base parar borrar...";
            }
            else{
                int reply = JOptionPane.showConfirmDialog(null,"Esta seguro que desea eliminar la base "+base+"?","input",JOptionPane.YES_NO_OPTION);
                if(reply == JOptionPane.YES_OPTION){
                    respuesta = dbad.eliminarBase(base);
                    dbgui.inactivaBotones();
                }
                else{
                    respuesta = "Operacion cancelada...";
                }
            }
            bEliminar.setEnabled(false);
            taDatos.setText(respuesta);
        }
        
        if(e.getSource() == bConsultar){
            datos = dbad.consultarBase();
            if(datos.equals("LISTA_VACIA")){
                datos = "La lista de bases se encuentra vacia...";
            }
            taDatos.setText(datos);
        }
        
    }
    /*
    public static void main(String args[]){
        new BaseGUI(null);
    }*/
}
