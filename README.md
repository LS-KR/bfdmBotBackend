
# bfdmBotBackend

A list backend which use BloomFilter request by @MoYoez.

## Quick start

```shell
java -jar ./bfdmBotBackend-1.0-SNAPSHOT-all.jar
```

use `Ctrl + C` (`^C`) or type `exit` to stop server.

## Setup

A config file will be generated automatically.

An example config:

```ini
[DEFAULT]
encrypt = SHA-256
size = 2147483647
auth = Your_Authentication_Key_Here
```

args:

```text
-c <config path>    Specify where the config file is, default as ./config.ini
-p <port>           Specify the port, default as 8964
-d <database port>  Specify where the database is, default as ./list.bfdb
```

## Frontend

POST to the server to check, add or delete.

interface:
- `method`: String, can be `check`, `add` or `del`.
- `id`: String, the id or value you want to check, add or delete.

### check

`method` should be `check` (Needless to say).  

Header: none  

response interface:
- `result`: Boolean, whether the value or id you check was in the database or list.

#### example

```shell
curl -X 'POST' 'http://127.0.0.1:8964/record.json' -d '{"method":"check","id":"12345678"}'
```

response will be

```json
{"result":false}
```

### add

`method` should be `add`  

Header:
- `Authentication`: Your authentication key.

response interface:
- `result`: String, could be `success` or `failed` and the reason why it failed.

#### example

```shell
curl -X 'POST' 'http://127.0.0.1:8964/record.json' -d '{"method":"add","id":"12345678"}' -H "Authentication: Your_Authentication_Key_Here"
```

response will be

```json
{"result":"success"}
```

### delete

`method` should be `del`

Header:
- `Authentication`: Your authentication key.

response interface:
- `result`: String, could be `success` or `failed` and the reason why it failed.

#### example

```shell
curl -X 'POST' 'http://127.0.0.1:8964/record.json' -d '{"method":"del","id":"12345678"}' -H "Authentication: Your_Authentication_Key_Here"
```

response will be

```json
{"result":"success"}
```
