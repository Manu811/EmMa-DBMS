public class RegistroDP{
	//Atributos: Variables de Instancia o de Clase
	private String registro;
	private RegistroDP next;

	//Constructores
	public RegistroDP(){
		this.registro = "";
	}
	
	public RegistroDP(String datos){
		this.registro = datos;
	}
	
	//Metodos: Accesors (geter's) y Mutators (seter's)

	//Accesor o geter es un metodo o un servicio que permite obtener el valor de una variable de instancia o atributo.
	public String getRegistro(){
		return this.registro;
	}
    public RegistroDP getNext(){
    	return this.next;
    }

	//Mutator o seter es un metodo o un servicio que permite cambiar el valor de una variable de instancia o atributo.
	public void setRegistro(String reg){
		this.registro = reg;
	}
    public void setNext(RegistroDP nodo){
    	this.next = nodo;
    }
	
	public String toString(){
		return this.registro;
	}
}