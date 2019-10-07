### To reproduce

Run Main.java

Send rpush message to a key using a redis-cli example:

```bash
redis-cli rpush testA x
``` 

It will print "testeA"

Now drop the redis-server manually, example 
```bash
systemctl stop redis
```

And run it again
```bash
systemctl start redis
```

Lettuce will be able to reconnect successfully but it will not re-subscribe.

Now, just send another rpush and nothing will be printed.