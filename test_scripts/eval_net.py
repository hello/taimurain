#!/usr/bin/python
import neural_net_messages_pb2
import requests


m = neural_net_messages_pb2.NeuralNetInput()

m.net_id = 'SLEEP'

for i in range(7):
    d = m.mat.add()
    d.vec.append(0.0)
    d.id = str(i)




response = requests.post('http://127.0.0.1:5555/v1/neuralnet/evaluate',data=m.SerializeToString())

a = neural_net_messages_pb2.NeuralNetOutput()
a.ParseFromString(response.content)
print response.status_code
print a.mat[0].vec
