#!/usr/bin/python
import neural_net_messages_pb2
import requests
import time

m = neural_net_messages_pb2.NeuralNetInput()

m.net_id = 'SLEEP'

for t in range(961):  
    d = m.mat.add()
    d.id = str(t)
    for i in range(8):
        d.vec.append(0.0)



t1 =  time.time()
response = requests.post('http://127.0.0.1:5555/v1/neuralnet/evaluate',data=m.SerializeToString())
t2 = time.time()
a = neural_net_messages_pb2.NeuralNetOutput()
a.ParseFromString(response.content)
print response.status_code
print len(a.mat)
print a.mat[0].vec
print t2 - t1

