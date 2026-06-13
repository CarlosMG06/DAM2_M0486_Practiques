# Fitxa de Requisits: Sistema de Registre d'Esdeveniments de Joc 2D amb WebSockets

**Autor:** Carlos Medina Gálvez  
**Data:** 2026-06-13  

## 1. Objectiu Principal

Desenvolupar un servidor WebSocket en Node.js que rebi moviments 2D d'un jugador des d'un client, emmagatzemi cada moviment a MongoDB, detecti quan una partida finalitza (10 segons sense moviment), calculi la distància entre punt inicial i final, i notifiqui el resultat al client.

## 2. Requisits Funcionals (RF)

### Connexions i comunicació

| ID | Requisit | Prioritat |
|----|----------|-----------|
| **RF-01** | El servidor WebSocket ha d'escoltar connexions entrants en un port configurable (per defecte 8080) | Alta |
| **RF-02** | El client ha d'establir connexió WebSocket amb el servidor | Alta |
| **RF-03** | El servidor ha de gestionar múltiples connexions simultàniament | Alta |
| **RF-04** | El servidor ha de tancar la connexió de manera graceful i registrar l'esdeveniment | Mitjana |

### Captura i enviament de moviments

| ID | Requisit | Prioritat |
|----|----------|-----------|
| **RF-05** | El client ha de capturar les tecles de fletxa (amunt, avall, esquerra, dreta) | Alta |
| **RF-06** | Cada tecla premuda ha d'incrementar/decrementar la posició en 1 unitat en l'eix corresponent | Alta |
| **RF-07** | El client ha d'enviar al servidor un missatge JSON amb la posició actual | Alta |
| **RF-08** | El format del missatge ha d'incloure: `sessionId`, `playerId`, `position {x, y}`, `timestamp` | Alta |

### Emmagatzematge a MongoDB

| ID | Requisit | Prioritat |
|----|----------|-----------|
| **RF-09** | El servidor ha d'emmagatzemar cada moviment rebut com un document individual a MongoDB | Alta |
| **RF-10** | Cada document ha d'incloure: `sessionId`, `playerId`, `movementNumber`, `position`, `previousPosition`, `distanceFromPrevious`, `timestamp`, `receivedAt` | Alta |
| **RF-11** | Tots els moviments d'una mateixa partida han de compartir el mateix `sessionId` | Alta |
| **RF-12** | En finalitzar la partida, s'ha de guardar un document resum amb estadístiques globals | Mitjana |

### Gestió de partides

| ID | Requisit | Prioritat |
|----|----------|-----------|
| **RF-13** | El servidor ha de considerar que la partida continua mentre rep moviments amb el mateix `sessionId` | Alta |
| **RF-14** | Si passen 10 segons sense rebre cap moviment, la partida es considera finalitzada | Alta |
| **RF-15** | El timer d'inactivitat s'ha de reiniciar amb cada moviment rebut | Alta |
| **RF-16** | El client ha de poder iniciar una nova partida amb un nou `sessionId` sense reiniciar el programa | Mitjana |

### Càlculs i notificacions

| ID | Requisit | Prioritat |
|----|----------|-----------|
| **RF-17** | En finalitzar la partida, el servidor ha de calcular la distància en línia recta entre el primer i l'últim moviment (fórmula euclidiana) | Alta |
| **RF-18** | El servidor ha d'enviar al client un missatge amb el resultat de la partida | Alta |
| **RF-19** | El missatge de final de partida ha d'incloure: `sessionId`, `distance`, `totalMovements`, `duration`, `startPosition`, `endPosition` | Alta |

---

## 3. Requisits No Funcionals (RNF)

### Logging

| ID | Requisit | Prioritat |
|----|----------|-----------|
| **RNF-01** | El servidor ha d'implementar logs utilitzant la llibreria Winston | Alta |
| **RNF-02** | Els logs s'han de mostrar per consola i guardar en un fitxer | Alta |
| **RNF-03** | S'han de registrar els esdeveniments: inici del servidor, connexió/desconnexió de clients, moviments rebuts, final de partida, errors | Alta |
| **RNF-04** | El format del log ha d'incloure timestamp, nivell i missatge | Mitjana |

### Rendiment i escalabilitat

| ID | Requisit | Prioritat |
|----|----------|-----------|
| **RNF-05** | El servidor ha de gestionar múltiples partides simultàniament sense interferències | Alta |
| **RNF-06** | Cada partida ha de tenir el seu propi timer d'inactivitat independent | Alta |
| **RNF-07** | La latència entre moviment i emmagatzematge ha de ser inferior a 100ms en condicions normals | Mitjana |

### Mantenibilitat

| ID | Requisit | Prioritat |
|----|----------|-----------|
| **RNF-08** | La configuració (port, URI MongoDB, timeout, noms de col·leccions) ha de ser modificable mitjançant variables d'entorn (fitxer `.env`) | Alta |
| **RNF-09** | El codi ha d'estar comentat en català/castellà/anglès explicant les parts importants | Mitjana |
| **RNF-10** | Els missatges de log han de ser clars i descriptius per facilitar la depuració | Mitjana |

### Fiabilitat

| ID | Requisit | Prioritat |
|----|----------|-----------|
| **RNF-11** | El servidor ha de gestionar errors de connexió a MongoDB sense aturar-se completament | Alta |
| **RNF-12** | El servidor ha de validar l'estructura dels missatges rebuts i rebutjar els invàlids | Mitjana |
| **RNF-13** | El client ha de gestionar la desconnexió inesperada del servidor de manera graceful | Baixa |

---

## 4. Format dels Missatges JSON

### Client → Servidor (moviment)

```json
{
  "sessionId": "550e8400-e29b-41d4-a716-446655440000",
  "playerId": "player_001",
  "position": {
    "x": 5,
    "y": 3
  },
  "timestamp": "2026-06-13T10:30:00.000Z"
}
```

## Servidor → Client (confirmació)

```json
{
  "type": "movement_received",
  "sessionId": "550e8400-e29b-41d4-a716-446655440000",
  "movementCount": 42
}
```

## Servidor → Client (final de partida)

```json
{
  "type": "game_over",
  "sessionId": "550e8400-e29b-41d4-a716-446655440000",
  "distance": 7.07,
  "totalMovements": 42,
  "duration": 12500,
  "startPosition": {
    "x": 0,
    "y": 0
  },
  "endPosition": {
    "x": 5,
    "y": 5
  },
  "reason": "inactivity_timeout"
}
```

Servidor → Client (error)

```json
{
  "type": "error",
  "message": "Invalid message format"
}
```