#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <ctype.h>

int main(int argc, char *argv[]) {
    const char protocolo[] = "tcp";
    int socket_cliente;
    struct sockaddr_in dir_router;  // Información del router
    unsigned short puerto_router = 5000u;
    char *direccion_router = "127.0.0.1";  // Dirección del router

    struct sockaddr_in dir_servidor;
    unsigned short puerto_servidor = 4500u;
    char *direccion_servidor;
    struct protoent *protoent;
    struct hostent *hostent;
    in_addr_t in_addr;

    if (argc > 1)
        direccion_servidor = argv[1];
    else {
        perror("Debe proporcionar un parámetro con la dirección del servidor");
        exit(-1);
    }

    // Obtiene la configuración para el socket TCP
    protoent = getprotobyname(protocolo);

    // Crea el socket del dominio Internet, para comunicaciones orientadas a conexión y con el protocolo seleccionado
    socket_cliente = socket(AF_INET, SOCK_STREAM, protoent->p_proto);

    // Obtiene la dirección IP del router
    hostent = gethostbyname(direccion_router);
    in_addr = inet_addr(inet_ntoa(*(struct in_addr *)*(hostent->h_addr_list)));

    // Configura la dirección en la que escucha el router
    dir_router.sin_family = AF_INET;
    dir_router.sin_addr.s_addr = in_addr;
    dir_router.sin_port = htons(puerto_router);

    // Realiza la conexión del cliente al router
    if (connect(socket_cliente, (struct sockaddr *)&dir_router, sizeof(struct sockaddr_in)) == -1) {
        perror("Error en la conexión al router");
        exit(-1);
    }

    // Envía la dirección del servidor al router
    write(socket_cliente, direccion_servidor, strlen(direccion_servidor));

    // Cierra el socket del cliente al router
    close(socket_cliente);

    // Continuar con la lógica original del cliente para conectarse al servidor, enviar y recibir datos.

    // Crear un nuevo socket para conectarse al servidor final
    int socket_servidor = socket(AF_INET, SOCK_STREAM, protoent->p_proto);

    // Obtiene la dirección IP del servidor final
    hostent = gethostbyname(direccion_servidor);
    in_addr = inet_addr(inet_ntoa(*(struct in_addr *)*(hostent->h_addr_list)));

    // Configura la dirección en la que escucha el servidor final
    dir_servidor.sin_family = AF_INET;
    dir_servidor.sin_addr.s_addr = in_addr;
    dir_servidor.sin_port = htons(puerto_servidor);

    // Realiza la conexión del cliente al servidor final
    if (connect(socket_servidor, (struct sockaddr *)&dir_servidor, sizeof(struct sockaddr_in)) == -1) {
        perror("Error en la conexión al servidor");
        exit(-1);
    }

    char buffer[200];
    int nbytes = 0;

    // Limpia el buffer
    memset(buffer, 0, 200);

    // Se lee una cadena desde el teclado
    printf("Ingrese una cadena: ");
    fgets(buffer, 200, stdin);

    nbytes = strlen(buffer);

    // Se envía la cadena al servidor final
    write(socket_servidor, buffer, nbytes);

    // Se lee la respuesta del servidor final
    nbytes = read(socket_servidor, buffer, 200);

    // Se imprime la cadena leída desde la conexión
    printf("El servidor ha respondido: %s", buffer);

    // Se cierra el socket cliente al servidor final
    close(socket_servidor);

    return 0;
}
