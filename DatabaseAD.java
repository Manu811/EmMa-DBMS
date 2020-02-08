import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DatabaseAD{
	
	// Atributos
	private BaseDP actualB;
	private TablaDP actualT;
	private RegistroDP actualR;
	private int nodoActual;

	private LinkedList listaBases = new LinkedList();
	private LinkedList listaTablas;
	private LinkedList listaRegistros;
	
	// Constructor
	public DatabaseAD(){
		datosArchivoLista();
	}

	public TablaDP getActualT(){
		return this.actualT;
	}

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

	public String getBaseActual(){
		String baseActual = "";
		try{
			BufferedReader archivoIn = new BufferedReader(new FileReader("BaseActual.txt"));
			
			while(archivoIn.ready()){
				baseActual = archivoIn.readLine();
			}
			archivoIn.close();
		}
		catch(FileNotFoundException fnfe){
			System.out.println("Error: "+fnfe);
		}
		catch(IOException ioe){
			System.out.println("Error: "+ioe);
		}
		return baseActual;
	}
	
	private String crearNodoBase(String datos){
		String respuesta = "";
		actualB = new BaseDP(datos);
		actualB.setLinkedList(listaTablas);
		listaBases.add(actualB);
		System.out.println(listaBases);
		
		respuesta = "Nuevo nodo creado en la Lista de Bases:\n"+datos;
		
		return respuesta;
	}

	public String consultarNombreBase(String nombre){
		String datos = "";
		int i = 0;
		boolean encontrado = false;
		if(listaBases.isEmpty()){
			datos = "LISTA_VACIA";
		}
		else{
			while(i < listaBases.size() && !encontrado){
				actualB = (BaseDP)listaBases.get(i);

				if(nombre.equals(actualB.getNombre())){
					datos = actualB.toString();
					encontrado = true;
					nodoActual = i;
				}
				i++;
			}
			if(!encontrado){
				datos = "NOT_FOUND";
			}
		}
		return datos;
	}

	public String crearBase(String nombre){
		String respuesta = "";
		
		String resultado = consultarNombreBase(nombre);

		if(resultado.equals("LISTA_VACIA") || resultado.equals("NOT_FOUND")){
			listaTablas  = new LinkedList();
			respuesta = crearNodoBase(nombre);
		}
		else{
			respuesta = "El nombre "+nombre+" ya ha sido usado en otra base de datos...";
		}		
		return respuesta;
	}

	public String consultarBase(){
		String datos = "";
		int i = 0;
		System.out.println(listaBases);
		if(listaBases.isEmpty()){
			datos = "Lista de bases vacia...";
		}
		else{
			while(i < listaBases.size()){
				actualB = (BaseDP)listaBases.get(i);
				datos = datos + actualB.getNombre() + "\n";
				i++;
			}
		}
		return datos;
	}

	public String eliminarBase(String base){
		String respuesta = "";
		int i = 0;
		LinkedList tablasBaseActual;
		setBaseActual(base);
		respuesta = consultarNombreBase(base);
		if(respuesta.equals("LISTA_VACIA") || respuesta.equals("NOT_FOUND")){
			respuesta = "Base de datos no encontrada"; 
		}
		else{
			actualB = (BaseDP)listaBases.get(nodoActual);
			tablasBaseActual = actualB.getLinkedList();

			while(!tablasBaseActual.isEmpty()){
				i = 0;
				while(i < tablasBaseActual.size()){
					actualT = (TablaDP)tablasBaseActual.get(i);
					eliminarTabla(actualT.getNombre());
					i++;
				}
			}

			try{
				if(Files.deleteIfExists(Paths.get("bases/"+base))){
					System.out.println(base+" se ha borrado del directorio...");
				}
				else{
					System.out.println(base+" no pudo ser borrado...");
				}
			}
			catch(NoSuchFileException nsfe){
				System.out.println("Error: "+nsfe);
			}
			catch(IOException ioe){
				System.out.println("Error: "+ioe);
			}

			listaBases.remove(nodoActual);
			respuesta = "Nodo eliminado de la lista:\n"+actualB.toString();
		}

		return respuesta;
	}

	public String crearNodoTabla(String datos){
		String respuesta = "";
		String base = getBaseActual();
		LinkedList tablasBaseActual;

		String resultado = consultarNombreBase(base);
		if(resultado.equals("LISTA_VACIA") || resultado.equals("NOT_FOUND")){
			respuesta = "Base de datos no encontrada";
		}
		else{
			actualB = (BaseDP)listaBases.get(nodoActual);
			tablasBaseActual = actualB.getLinkedList();
				
			actualT = new TablaDP(datos);
			actualT.setLinkedList(listaRegistros);
			tablasBaseActual.add(actualT);
			System.out.println(listaBases);
			
			respuesta = "Nuevo nodo creado en la Lista de Tablas:\n"+datos;
		}
		return respuesta;
	}

	public String consultarNombreTabla(String nombre){
		String datos = "";
		int i = 0;
		boolean encontrado = false;
		String base = getBaseActual();
		LinkedList tablasBaseActual;

		String resultado = consultarNombreBase(base);
		if(resultado.equals("LISTA_VACIA") || resultado.equals("NOT_FOUND")){
			datos = "Base de datos no encontrada";
		}
		else{
			actualB = (BaseDP)listaBases.get(nodoActual);
			tablasBaseActual = actualB.getLinkedList();

			if(tablasBaseActual.isEmpty()){
				datos = "LISTA_VACIA";
			}
			else{
				while(i < tablasBaseActual.size() && !encontrado){
					actualT = (TablaDP)tablasBaseActual.get(i);
	
					if(nombre.equals(actualT.getNombre())){
						datos = actualT.toString();
						encontrado = true;
						nodoActual = i;
					}
					i++;
				}
				if(!encontrado){
					datos = "NOT_FOUND";
				}
			}
		}
		return datos;
	}

	public String crearTabla(String nombre_tabla, int nCampos, String[] nombre_campos){
		String respuesta = "";
		String nombrec = "";
		int i = 0;

		String resultado = consultarNombreTabla(nombre_tabla);
		
		if(resultado.equals("LISTA_VACIA") || resultado.equals("NOT_FOUND")){
			for(i = 0; i < nCampos-1; i++){
				nombrec = nombrec + nombre_campos[i]+"-";
			}
			nombrec = nombrec + nombre_campos[nCampos-1];
			String datos = nombre_tabla+"_"+nCampos+"_"+nombrec;
			
			listaRegistros = new LinkedList();
			respuesta = crearNodoTabla(datos);
		}
		else{
			respuesta = "El nombre "+nombre_tabla+" ya ha sido usado en alguna tabla de la base de datos...";
		}		
		return respuesta;
	}

	public String consultarTabla(){
		String datos = "";
		int i = 0;
		String base = getBaseActual();
		LinkedList tablasBaseActual;

		String resultado = consultarNombreBase(base);
		if(resultado.equals("LISTA_VACIA") || resultado.equals("NOT_FOUND")){
			datos = "Base de datos no encontrada";
		}
		else{
			actualB = (BaseDP)listaBases.get(nodoActual);
			tablasBaseActual = actualB.getLinkedList();

			if(tablasBaseActual.isEmpty()){
				datos = "LISTA_VACIA";
			}
			else{
				while(i < tablasBaseActual.size()){
					actualT = (TablaDP)tablasBaseActual.get(i);
					datos = datos + actualT.getNombre()+"\n";
					i++;
				}
			}
		}
		return datos;
	}

	public String eliminarTabla(String tabla){
		String respuesta = "";
		String base = getBaseActual();
		LinkedList tablasBaseActual;

		respuesta = consultarNombreBase(base);
		if(respuesta.equals("LISTA_VACIA") || respuesta.equals("NOT_FOUND")){
			respuesta = "Base de datos no encontrada"; 
		}
		else{
			actualB = (BaseDP)listaBases.get(nodoActual);
			tablasBaseActual = actualB.getLinkedList();
			respuesta = consultarNombreTabla(tabla);
			if(respuesta.equals("LISTA_VACIA") || respuesta.equals("NOT_FOUND")){
				respuesta = "Tabla no encontrada"; 
			}
			else{
				actualT = (TablaDP)tablasBaseActual.get(nodoActual);
				tablasBaseActual.remove(nodoActual);
				
				/*
				File file = new File("bases/"+base+"/"+actualT.getNombre()+"_"+actualT.getNCampos()+"_"+actualT.getNombre_Campos()+".txt");
				
				if(file.delete()){
					System.out.println(actualT.getNombre()+"_"+actualT.getNCampos()+"_"+actualT.getNombre_Campos()+".txt Se ha borrado del directorio...");
				}
				else{
					System.out.println(actualT.getNombre()+"_"+actualT.getNCampos()+"_"+actualT.getNombre_Campos()+".txt No pudo ser borrado...\n");
				}
				*/

				try{
					if(Files.deleteIfExists(Paths.get("bases/"+base+"/"+actualT.getNombre()+"_"+actualT.getNCampos()+"_"+actualT.getNombre_Campos()+".txt"))){
						System.out.println(actualT.getNombre()+"_"+actualT.getNCampos()+"_"+actualT.getNombre_Campos()+".txt se ha borrado del directorio "+base+"...");
					}
					else{
						System.out.println(actualT.getNombre()+"_"+actualT.getNCampos()+"_"+actualT.getNombre_Campos()+".txt del directorio "+base+" no pudo ser borrado...");
					}
				}
				catch(NoSuchFileException nsfe){
					System.out.println("Error: "+nsfe);
				}
				catch(IOException ioe){
					System.out.println("Error: "+ioe);
				}

				respuesta = "Nodo eliminado de la lista:\n"+actualT.toString();
			}
		}
		return respuesta;
	}

	public String crearNodoRegistro(String nombre_tabla, String datos){
		String respuesta = "";
		String base = getBaseActual();
		LinkedList tablasBaseActual;
		LinkedList registrosTablaActual;

		String resultado = consultarNombreBase(base);
		if(resultado.equals("LISTA_VACIA") || resultado.equals("NOT_FOUND")){
			respuesta = "Base de datos no encontrada";
		}
		else{
			actualB = (BaseDP)listaBases.get(nodoActual);
			tablasBaseActual = actualB.getLinkedList();

			resultado = consultarNombreTabla(nombre_tabla);
			if(resultado.equals("LISTA_VACIA") || resultado.equals("NOT_FOUND")){
				respuesta = "Tabla no encontrada";
			}
			else{
				actualT = (TablaDP)tablasBaseActual.get(nodoActual);
				registrosTablaActual = actualT.getLinkedList();
				registrosTablaActual.add(new RegistroDP(datos));
				System.out.println(listaBases);
				respuesta = "Nuevo nodo creado en la Lista de Tablas:\n"+datos;
			}
		}
		return respuesta;
	}

	public String crearRegistro(String nombre_tabla, String[] nombre_campos, int nCampos){
		String respuesta = "";
		String registro = "";
		int i = 0;
		
		for(i = 0; i < nCampos-1; i++){
			registro = registro + nombre_campos[i]+"_";
		}
		registro = registro + nombre_campos[nCampos-1];

		respuesta = crearNodoRegistro(nombre_tabla,registro);
				
		return respuesta;
	}

	public String consultarRegistro(String tabla){
		
		String datos = "";
		int i = 0;
		String base = getBaseActual();
		LinkedList tablasBaseActual;
		LinkedList registrosTablaActual;

		String resultado = consultarNombreBase(base);
		if(resultado.equals("LISTA_VACIA") || resultado.equals("NOT_FOUND")){
			datos = "Base de datos no encontrada";
		}
		else{
			actualB = (BaseDP)listaBases.get(nodoActual);
			tablasBaseActual = actualB.getLinkedList();

			resultado = consultarNombreTabla(tabla);
			if(resultado.equals("LISTA_VACIA") || resultado.equals("NOT_FOUND")){
				datos = "Tabla no encontrada";
			}
			else{
				actualT = (TablaDP)tablasBaseActual.get(nodoActual);
				registrosTablaActual = actualT.getLinkedList();

				if(registrosTablaActual.isEmpty()){
					datos = "LISTA_VACIA";
				}
				else{
					//actualR = (RegistroDP)registrosTablaActual.get(nodoActual);
					while(i < registrosTablaActual.size()){
						actualR = (RegistroDP)registrosTablaActual.get(i);
						datos = datos + actualR.toString()+"\n";
						i++;
					}
				}
			}
		}
		return datos;
	}

	public void datosListaArchivo(){
		PrintWriter archivoOut;
		int i, j, k;
		System.out.println("\nDatos a Guardar:\n"+listaBases+"\n");

		LinkedList listaT, listaR;

		if(listaBases.isEmpty()){
			System.out.println("No se han creado archivos, Lista de Bases vacia...");
		}
		else{
			i = 0;
			while(i < listaBases.size()){
				actualB = (BaseDP)listaBases.get(i);
				File dir = new File("bases/"+actualB.getNombre());
				boolean test = dir.mkdir();

				if(!test){
					System.out.println("No se creo el directorio "+actualB.getNombre()+"...");
				}

				//if(test){
					listaT = actualB.getLinkedList();

					if(listaT.isEmpty()){
						System.out.println("La Base "+actualB.getNombre()+" no tiene tablas...");
					}
					else{
						j = 0;
						while(j < listaT.size()){
							actualT = (TablaDP)listaT.get(j);
							listaR = actualT.getLinkedList();
							if(listaR.isEmpty()){
								System.out.println("La Tabla "+actualT.getNombre()+" de la base "+actualB.getNombre()+" no tiene registros...");
							}
							try{
								archivoOut = new PrintWriter(new FileWriter("bases/"+actualB.getNombre()+"/"+actualT.getNombre()+"_"+actualT.getNCampos()+"_"+actualT.getNombre_Campos()+".txt"));
								k = 0;
								while(k < listaR.size()){
									actualR = (RegistroDP)listaR.get(k);
									archivoOut.println(actualR.toString());
									k++;
								}
								archivoOut.close();
							}
							catch(IOException ioe){
								System.out.println("Error: "+ioe);
							}
							j++;
						}
					}
				//}
				//else{
				//	System.out.println("No se creo el directorio "+actualB.getNombre()+"...\n");
				//}
				i++;
			}
		}
	}

	public void datosArchivoLista(){
		int i,j;
		File fileBase = new File("bases/");
		String bases[] = fileBase.list();

		i = 0;
		while(i <  bases.length){
			listaTablas  = new LinkedList();
			crearNodoBase(bases[i]);
			setBaseActual(bases[i]);
			
			File fileTabla = new File("bases/"+bases[i]+"/");
			String tablas[] = fileTabla.list();

			j = 0;
			while(j < tablas.length){
				listaRegistros = new LinkedList();
				StringTokenizer st  = new StringTokenizer(tablas[j],".");
				String tabla = st.nextToken();
				crearNodoTabla(tabla);

				try{
					String nombre_tabla  = new StringTokenizer(tabla,"_").nextToken();

					BufferedReader archivoIn = new BufferedReader(new FileReader("bases/"+bases[i]+"/"+tablas[j]));
					
					while(archivoIn.ready()){
						crearNodoRegistro(nombre_tabla, archivoIn.readLine());
					}
					
					archivoIn.close();
				}
				catch(FileNotFoundException fnfe){
					System.out.println("Error: "+fnfe);
				}
				catch(IOException ioe){
					System.out.println("Error: "+ioe);
				}
				j++;
			}
			i++;
		}
		System.out.println("\nDatos Guardados:\n"+listaBases+"\n");
	}
}