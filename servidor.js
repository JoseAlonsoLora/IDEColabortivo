/*
Identificador del programa: SERVIDORNODEIDE2017             

Clave única del programa: SNIDE2017                           
                                                      
Nombre: Servidor Node IDE Colaborativo                   

Nombres de los desarrolladores: 
                                Jose Alonso Lora González.
                                Raymundo de Jesús Perez Caastellanos.                                 
                                                                                                     
Fecha en la que se inicio el programa : 20 de septiembre de 2017. 

                                                        
Descripción: Servidor que permite comunicar mediante eventos a los colaboradores de un proyecto.  
*/
var io = require("socket.io")(9000);
var programadoresConectados = [];
var Colaborador = {
    nombre:"",
    lobby:""
};

/**
     * Permite obtener la lobby del colaborador
     *
     * @param colaborador nombre del colaborador 
     * @return regresa la lobby del colaborador.
     */
function buscarColaborador(colaborador){
	for(i = 0;i<programadoresConectados.length;i++){
		if(colaborador === programadoresConectados[i].nombre){
			return programadoresConectados[i].lobby;
		}
	}
	return null;
}

io.on("connection",function(socket){

	socket.on("agregarNombre",function(colaborador){
		socket.username = colaborador;
		socket.room = socket.id;
		Colaborador = new Object();
		Colaborador.nombre = colaborador;
		Colaborador.lobby = socket.id;
        programadoresConectados.push(Colaborador);
		
	});

	socket.on("conectarseALobby",function(lobby){
		socket.room = lobby;
        socket.join(lobby);
		socket.broadcast.to(lobby).emit("colaboradorConectado", socket.username);
	});

	socket.on("invitarColaborador",function(colaborador,proyecto){
		var lobby = buscarColaborador(colaborador);
		if(lobby === null){
			socket.emit("colaboradorNoEncontrado");
		}else{
			if(lobby === socket.room){
				socket.emit("mensajeRecursivo");
			}else{
				socket.emit("colaboradorEncontrado");
				socket.broadcast.to(lobby).emit("mostrarInvitaion", socket.room, socket.username, proyecto);
			}
		}
		
	});
	
	socket.on("terminarSesionHost",function(){
		socket.broadcast.to(socket.room).emit("terminarSesion");
	});
	
	socket.on("terminarSesionInvitado",function(){
		socket.broadcast.to(socket.room).emit("mensajeSalida");
		socket.leave(socket.room);
		socket.room = socket.id;
	});
	
	socket.on("terminarSesion",function(){
		socket.leave(socket.room);
		socket.room = socket.id;
	});
	
	socket.on("cerrarSesion",function(colaborador){
		var posicion;
		for(i = 0;i<programadoresConectados.length;i++){
			if(colaborador === programadoresConectados[i].nombre){
				posicion = i;
				break;
			}
		}
		programadoresConectados.splice(posicion,1);
	});
	
	socket.on("escribirCodigo",function(texto,ruta){
		if (socket.room === socket.id){	
			socket.broadcast.to(socket.room).emit("escribirCodigoInvitado",texto,ruta);
		}else{
			socket.broadcast.to(socket.room).emit("escribirCodigoHost",texto,ruta);
		}
	});
	
	socket.on("abrirTab",function(archivo){
		if (socket.room === socket.id){	
			socket.broadcast.to(socket.room).emit("abrirTabInvitado",archivo);
		}else{
			socket.broadcast.to(socket.room).emit("abrirTabHost",archivo);
		}
	});
	
	socket.on("mensajeCompilacionExitosa",function(){
			socket.broadcast.to(socket.room).emit("mensajeCompilacionExitosa");		
	});
	
	socket.on("mensajeErrorCompilacion",function(resultado){	
			socket.broadcast.to(socket.room).emit("mensajeErrorCompilacion",resultado);
	});
	
	socket.on("resultadoEjecucion",function(resultado){	
			socket.broadcast.to(socket.room).emit("resultadoEjecucion",resultado);
	});
	
	socket.on("agregarPaquete",function(carpeta){	
			socket.broadcast.to(socket.room).emit("agregarPaquete",carpeta);
	});
	
	socket.on("agregarArchivo",function(archivo,rutaCarpeta){	
			socket.broadcast.to(socket.room).emit("agregarArchivo",archivo,rutaCarpeta);
	});
	
	socket.on("eliminarPaquete",function(nombreCarpeta){	
			socket.broadcast.to(socket.room).emit("eliminarPaquete",nombreCarpeta);
	});
	
	socket.on("eliminarArchivo",function(nombreCarpeta,nombreArchivo){	
			socket.broadcast.to(socket.room).emit("eliminarArchivo",nombreCarpeta,nombreArchivo);
	});
		

});

