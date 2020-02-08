import java.util.LinkedList;

public class BaseDP{
	//Atributos: Variables de Instancia o de Clase
	private String nombre;
	private LinkedList linkedList;
	private BaseDP next;
	
	//Constructores
	public BaseDP(){
		this.nombre = "";
	}
	
	public BaseDP(String datos){
		this.nombre = datos;
		this.linkedList = new LinkedList();
	}
	
	//Metodos: Accesors (geter's) y Mutators (seter's)

	//Accesor o geter es un metodo o un servicio que permite obtener el valor de una variable de instancia o atributo.
	public String getNombre(){
		return this.nombre;
	}
	public LinkedList getLinkedList(){
		return this.linkedList;
	}
    public BaseDP getNext(){
    	return this.next;
    }

	//Mutator o seter es un metodo o un servicio que permite cambiar el valor de una variable de instancia o atributo.
	public void setNombre(String nom){
		this.nombre = nom;
	}
	public void setLinkedList(LinkedList lili){
		this.linkedList = lili;
	}
    public void setNext(BaseDP nodo){
    	this.next = nodo;
    }
	
	public String toString(){
		return this.nombre+"_"+this.linkedList;
	}
}