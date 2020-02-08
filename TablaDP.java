import java.util.*;
	
public class TablaDP{
	//Atributos: Variables de Instancia o de Clase
    private String nombre;
    private String nombre_campos;
    private int nCampos;
	private LinkedList linkedList;
	private TablaDP next;
	
	//Constructores
	public TablaDP(){
		this.nombre = "";
        this.nCampos = 0;
        this.nombre_campos = "";
	}
	
	public TablaDP(String datos){
        StringTokenizer st  = new StringTokenizer(datos,"_");
        this.nombre         = st.nextToken();
        this.nCampos        = Integer.parseInt(st.nextToken());
		this.nombre_campos  = st.nextToken();
		this.linkedList     = new LinkedList();
	}
	
	//Metodos: Accesors (geter's) y Mutators (seter's)

	//Accesor o geter es un metodo o un servicio que permite obtener el valor de una variable de instancia o atributo.
	public String getNombre(){
		return this.nombre;
    }
    public int getNCampos(){
        return this.nCampos;
    }
	public String getNombre_Campos(){
		return this.nombre_campos;
	}
	public LinkedList getLinkedList(){
		return this.linkedList;
	}
    public TablaDP getNext(){
    	return this.next;
    }

	//Mutator o seter es un metodo o un servicio que permite cambiar el valor de una variable de instancia o atributo.
	public void setNombre(String nom){
		this.nombre = nom;
    }
    public void setNCampos(int num){
        this.nCampos = num;
    }
	public void setNombre_Campos(String nc){
		this.nombre_campos = nc;
	}
	public void setLinkedList(LinkedList lili){
		this.linkedList = lili;
	}
    public void setNext(TablaDP nodo){
    	this.next = nodo;
    }
	
	public String toString(){
		return this.nombre+"_"+this.nCampos+"_"+this.nombre_campos+"_"+this.linkedList;
	}
}