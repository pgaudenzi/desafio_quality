# Documentacion

## Hoteles
#### 1) Obtener un listado de todos los hoteles diponibles.

| Method | SIGN           |
| ------ | -------------- |
|  GET   | /api/v1/hotels |


#### 2) Obtener un listado de todos los hoteles disponibles en un determinado rango de fechas y según el destino seleccionado.

| Method | SIGN                                                                            |
| ------ | ------------------------------------------------------------------------------  |
|  GET   | /api/v1/hotels?dateFrom=dd/mm/aaaa&dateTo=dd/mm/aaaa&destination=destinationName|

Parametros:

| Parametros    | TIPO           | Descripcion                |
| --------------| -------------- |----------------------------|
|  Fecha Entrada| Date           | (dd/mm/aaaa) Ej: 23/02/2021|
|  Fecha Salida | Date           | (dd/mm/aaaa) Ej: 25/02/2021|
|  Destino      | String         |  Puerto Iguazú             |


#### 3) Reserva de hotel, indicando cantidad de personas, fecha de entrada, fecha de salida y tipo de habitacion

| Method | SIGN           |
| ------ | -------------- |
|  POST  | /api/v1/booking|

Parametros:

| Parametros         | TIPO              | Descripcion                |
| -------------------| --------------    |----------------------------|
|  Fecha Entrada     | Date              | (dd/mm/aaaa) Ej: 23/02/2021|
|  Fecha Salida      | Date              | (dd/mm/aaaa) Ej: 25/02/2021|
|  Destino           | String            |  Puerto Iguazú             |
|Cantidad de personas| int               |  Puerto Iguazú             |
|Tipo de habitacion  | String (en ingles)|  Puerto Iguazú             |

Payload:
```json
{
  "userName": "seba_gonzalez@unmail.com",
  "booking": {
    "dateFrom": "12/02/2021",
    "dateTo": "17/04/2021",
    "destination": "Buenos Aires",
    "hotelCode": "BH-0002",
    "peopleAmount": 2,
    "roomType": "DOUBLE",
    "people": [
      {
        "dni": "12345678",
        "name": "Pepito",
        "lastName": "Gomez",
        "birthDate": "10/11/1982",
        "mail": "pepitogomez@gmail.com"
      },
      {
        "dni": "13345678",
        "name": "Fulanito",
        "lastName": "Gomez",
        "birthDate": "10/11/1983",
        "mail": "fulanitogomez@gmail.com"
      }
    ],
    "paymentMethod": {
      "type": "DEBIT",
      "number": "1234-1234-1234-1234",
      "dues": 0
    }
  }
}
```

## Vuelos

#### 1) Obtener un listado de vuelos disponibles

| Method | SIGN           |
| ------ | -------------- |
|  GET   | /api/v1/flights|

#### 2) Obtener un listado de todos los vuelos disponibles en un determinado rango de fechas y según el destino y el origen seleccionados

| Method | SIGN                                                                                               |
| ------ | -------------------------------------------------------------------------------------------------  |
|  GET   | /api/v1/flights?dateFrom=dd/mm/aaaa&dateTo=dd/mm/aaaa&origin=Bogotá&destination=Buenos Aires|

Parametros:

| Parametros    | TIPO           | Descripcion                |
| --------------| -------------- |----------------------------|
|  Fecha Ida    | Date           | (dd/mm/aaaa) Ej: 16/02/2021|
|  Fecha Vuelta | Date           | (dd/mm/aaaa) Ej: 28/02/2021|
|  Origen       | String         |  Bogotá                    |
|  Destino      | String         |  Buenos Aires              |


#### 3) Reserva de vuelo, indicando cantidad de personas, origen, destino, fecha de ida y fecha de vuelta.

| Method | SIGN                      |
| ------ | ------------------------- |
|  POST  | /api/v1/flight-reservation|

Parametros:

| Parametros         | TIPO           | Descripcion                |
| --------------     | -------------- |----------------------------|
|  Fecha Ida         | Date           | (dd/mm/aaaa) Ej: 16/02/2021|
|  Fecha Vuelta      | Date           | (dd/mm/aaaa) Ej: 28/02/2021|
|  Origen            | String         |  Buenos Aires              |
|  Destino           | String         |  Puerto Iguazú             |
|Cantidad de Personas| int            |  Puerto Iguazú             |

Payload:
```json
{
  "userName": "seba_gonzalez@unmail.com",
  "booking": {
    "dateFrom": "10/02/2021",
    "dateTo": "14/02/2021",
    "origin": "Buenos Aires",
    "destination": "Puerto Iguazú",
    "flightNumber": "BAPI-1235",
    "seats": 2,
    "seatType": "ECONOMY",
    "people": [
      {
        "dni": "12345678",
        "name": "Pepito",
        "lastName": "Gomez",
        "birthDate": "10/11/1982",
        "mail": "pepitogomez@gmail.com"
      },
      {
        "dni": "13345678",
        "name": "Fulanito",
        "lastName": "Gomez",
        "birthDate": "10/11/1983",
        "mail": "fulanitogomez@gmail.com"
      }
    ],
    "paymentMethod": {
      "type": "CREDIT",
      "number": "1234-1234-1234-1234",
      "dues": 6
    }
  }
}
```

