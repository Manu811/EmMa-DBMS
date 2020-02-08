import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.StringTokenizer;

public class TablaGUI extends JFrame implements ActionListener{
    private static final long serialVersionUID = 1L;

    // Atributos
    private JTextField  tfTabla;
    private JButton		bCrear, bConfirmarCrear, bInsertar, bConfirmarInsertar, bEliminar, bConsultar, bConsultarRegistros;
    
    private JTextArea  taDatos;
    private JScrollPane spDatos;
    private JPanel     panel1, panel2, panelD;
    
    private DatabaseAD  dbad;

    private TablaDP actualT;

    private int         maxCampos = 10;
    private String      tabla;
    private int         nCampos;
    private JTextField  tfCampos[];

    // Constructor
    public TablaGUI(DatabaseAD db){
        // 1. Crear los objetos de los atributos\
        dbad = db;

        tfTabla             = new JTextField();
        bCrear              = new JButton("Crear Tabla");
        bConfirmarCrear     = new JButton("Confirmar Crear");
        bInsertar           = new JButton("Insertar en una tabla");
        bConfirmarInsertar  = new JButton("Confirmar Insertar");
        bEliminar           = new JButton("Eliminar Tabla");
        bConsultar          = new JButton("Consultar Tablas");
        bConsultarRegistros = new JButton("Consultar Registros de una tabla");

        // Adicionar addActionListener a lo JButtons
        bCrear.addActionListener(this);
        bConfirmarCrear.addActionListener(this);
        bInsertar.addActionListener(this);
        bConfirmarInsertar.addActionListener(this);
        bEliminar.addActionListener(this);
        bConsultar.addActionListener(this);
        bConsultarRegistros.addActionListener(this);

        bConfirmarCrear.setEnabled(false);
        bConfirmarInsertar.setEnabled(false);
        
        taDatos = new JTextArea(11,40);
        
        panel1 = new JPanel();
        panelD = new JPanel();
        panel2 = new JPanel();
        
        // 2. Definir los Layouts de los JPanels
        panel1.setLayout(new GridLayout(5,2));
        panel2.setLayout(new FlowLayout());
        
        // 3. Colocar los objetos de los atributos en los JPanels correspondientes
        panel1.add(new JLabel("Tabla:"));
        panel1.add(tfTabla);
        panel1.add(bCrear);
        panel1.add(bConfirmarCrear);
        panel1.add(bInsertar);
        panel1.add(bConfirmarInsertar);
        panel1.add(bEliminar);
        panel1.add(bConsultar);
        panel1.add(bConsultarRegistros);
        
        panel2.add(panel1);
        spDatos = new JScrollPane(taDatos);
        panel2.add(spDatos);
        panel2.add(panelD);

        add(panel2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JPanel getPanel2(){
        return this.panel2;
    }

    private void activaCrear(){
        tfTabla.setEnabled(false);
        bCrear.setEnabled(false);
        bConfirmarCrear.setEnabled(true);
        bInsertar.setEnabled(false);
        bEliminar.setEnabled(false);
        bConsultar.setEnabled(false);
        bConsultarRegistros.setEnabled(false);
    }

    private void inactivaCrear(){
        tfTabla.setEnabled(true);
        bCrear.setEnabled(true);
        bConfirmarCrear.setEnabled(false);
        bInsertar.setEnabled(true);
        bEliminar.setEnabled(true);
        bConsultar.setEnabled(true);
        bConsultarRegistros.setEnabled(true);
    }

    private void activaInsertar(){
        tfTabla.setEnabled(false);
        bCrear.setEnabled(false);
        bInsertar.setEnabled(false);
        bConfirmarInsertar.setEnabled(true);
        bEliminar.setEnabled(false);
        bConsultar.setEnabled(false);
        bConsultarRegistros.setEnabled(false);
    }

    private void inactivaInsertar(){
        tfTabla.setEnabled(true);
        bCrear.setEnabled(true);
        bInsertar.setEnabled(true);
        bConfirmarInsertar.setEnabled(false);
        bEliminar.setEnabled(true);
        bConsultar.setEnabled(true);
        bConsultarRegistros.setEnabled(true);
    }

    private String[] obtenerDatos(){
        String[] nombre_campo = new String[nCampos];
        int i = 0;
        for (i = 0; i < nCampos; i++){
            nombre_campo[i] = tfCampos[i].getText().trim();
        }
        return nombre_campo;
    }

    public void actionPerformed(ActionEvent e){
        String datos="";
        String respuesta="";

        if(e.getSource() == bCrear){
            int i = 0;

            panel2.setVisible(false);
            panel2.remove(spDatos);
            panel2.remove(panelD);
            panelD.removeAll();
            taDatos.setText("");

            tabla = tfTabla.getText();
            tabla.trim();

            if(tabla.isEmpty()){
            	respuesta = "Llene el campo con un nombre para la tabla...";
            }
			else{
                respuesta = dbad.consultarNombreTabla(tabla);
                if(respuesta.equals("LISTA_VACIA") || respuesta.equals("NOT_FOUND")){
                    respuesta = "";
                    String campos = JOptionPane.showInputDialog("Numero de campos: ");
                    campos.trim();
                    if(campos.equals("")){
                        datos = "VACIO";
                    }
                    else{
                        try{
                            if(Integer.parseInt(campos) > maxCampos){
                                datos = "MAX_CAMPOS";
                            }
                        }
                        catch(NumberFormatException nfe){
                            datos = "NO_NUMERICO";
                        }
                    }
                    
                    if(datos.equals("VACIO")){
                        JOptionPane.showMessageDialog(null,"Debe darse un numero de campos");
                    }
                    else if(datos.equals("MAX_CAMPOS")){
                        JOptionPane.showMessageDialog(null,"Supera el numero de campos permitido ["+maxCampos+"]");
                    }
                    else if(datos.equals("NO_NUMERICO")){
                        JOptionPane.showMessageDialog(null, "El numero de campos debe ser numerico");
                    }
                    else{
                        nCampos = Integer.parseInt(campos);
                        panelD.setLayout(new GridLayout(nCampos,2));
                        tfCampos = new JTextField[nCampos];

                        for(i = 0; i < nCampos; i++){
                            tfCampos[i] = new JTextField();
                            panelD.add(new JLabel ("Campo "+(i+1)));
                            panelD.add(tfCampos[i]);
                        }

                        activaCrear();
                    }
                }
                else{
                    respuesta = "El nombre "+tabla+" ya fue utilizado en alguna otra tabla de la base actual...";
                }
            }
            panel2.add(panelD);
            panel2.add(spDatos);
            taDatos.setText(respuesta);
            panel2.setVisible(true);
        }

        if(e.getSource() == bConfirmarCrear){
            String[] resultado = obtenerDatos();

            for(int i = 0; i < nCampos; i++){
                if(resultado[i].equals("")){
                    datos = "VACIO";
                }
            }
            if(datos.equals("VACIO")){
                taDatos.setText("Algun campo esta vacio...");
            }
            else{
                respuesta = dbad.crearTabla(tabla, nCampos, resultado);
                panel2.setVisible(false);
                panel2.remove(panelD);
                panel2.setVisible(true);
                tfTabla.setText("");
                taDatos.setText(respuesta);

                inactivaCrear();
            }
        }
        
        if(e.getSource() == bInsertar){
            int i = 0;

            panel2.setVisible(false);
            panel2.remove(spDatos);
            panel2.remove(panelD);
            panelD.removeAll();
            taDatos.setText("");

            tabla = tfTabla.getText();
            tabla.trim();

            if(tabla.isEmpty()){
            	respuesta = "Llene el campo con un nombre para insertar datos en la tabla...";
            }
			else{
                respuesta = dbad.consultarNombreTabla(tabla);
                if(respuesta.equals("LISTA_VACIA") || respuesta.equals("NOT_FOUND")){
                    respuesta = "La Tabla "+tabla+" no fue localizada";
                }
                else{
                    respuesta = "";
                    actualT = dbad.getActualT();
                    nCampos = actualT.getNCampos();
                    String nombres = actualT.getNombre_Campos();

                    panelD.setLayout(new GridLayout(nCampos,2));

                    StringTokenizer st = new StringTokenizer(nombres,"-");
                    String[] nombres_campos = new String[nCampos];
                    for(i = 0; i < nCampos; i++){
                        nombres_campos[i] = st.nextToken();
                    }

                    tfCampos = new JTextField[nCampos];

                    for(i = 0; i < nCampos; i++){
                        tfCampos[i] = new JTextField();
                        panelD.add(new JLabel (nombres_campos[i]));
                        panelD.add(tfCampos[i]);
                    }
                    activaInsertar();
                }
            }
            panel2.add(panelD);
            panel2.add(spDatos);
            panel2.setVisible(true);
            taDatos.setText(respuesta);
        }

        if(e.getSource() == bConfirmarInsertar){
            String[] resultado = obtenerDatos();

            for(int i = 0; i < nCampos; i++){
                if(resultado[i].equals("")){
                    datos = "VACIO";
                }
            }
            if(datos.equals("VACIO")){
                taDatos.setText("Algun campo esta vacio...");
            }
            else{
                respuesta = dbad.crearRegistro(tabla, resultado, nCampos);
                panel2.setVisible(false);
                panel2.remove(panelD);
                panel2.setVisible(true);
                tfTabla.setText("");
                taDatos.setText(respuesta);

                inactivaInsertar();
            }
        }

        if(e.getSource() == bEliminar){
            String tabla = tfTabla.getText();
            tabla.trim();

            if(tabla.isEmpty()){
            	respuesta = "Seleccione una tabla parar borrar...";
            }
            else{
                int reply = JOptionPane.showConfirmDialog(null,"Esta seguro que desea eliminar la tabla "+tabla+"?","input",JOptionPane.YES_NO_OPTION);
                if(reply == JOptionPane.YES_OPTION){
                    respuesta = dbad.eliminarTabla(tabla);
                }
                else{
                    respuesta = "Operacion cancelada...";
                }
            }
            taDatos.setText(respuesta);
            
        }
        
        if(e.getSource() == bConsultar){
            datos = dbad.consultarTabla();
            if(datos.equals("LISTA_VACIA")){
                datos = "La lista de tablas se encuentra vacia...";
            }
            taDatos.setText(datos);
        }

        if(e.getSource() == bConsultarRegistros){
            tabla = tfTabla.getText();
            tabla.trim();

            if(tabla.isEmpty()){
            	datos = "Llene el campo con un nombre para la tabla...";
            }
            else{
                datos = dbad.consultarRegistro(tabla);
                
                if(datos.equals("LISTA_VACIA")){
                    datos = "La lista de registros de la tabla "+tabla+"\nse encuentra vacia...";
                }
            }
            taDatos.setText(datos);
        }
        
    }
    /*
    public static void main(String args[]){
        new TablaGUI(null);
    }*/
}
