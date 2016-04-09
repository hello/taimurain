#!/usr/bin/python
from flask import Flask,url_for,request
from ConfigParser import ConfigParser
import logging
from keras.models import model_from_json
import neural_net_messages_pb2

config_section_server = 'server'
config_file_name = 'config.txt'
host_key = 'host-ip'
port_key = 'port'

app = Flask(__name__)

def list_routes():
    import urllib

    output = []
    for rule in app.url_map.iter_rules():
        methods = ','.join(rule.methods)
        line = urllib.unquote("{:50s} {:20s} {}".format(rule.endpoint, methods, rule))
        output.append(line)

    return sorted(output)

@app.route('/')
def status():
    return '\n'.join(list_routes()) + '\n'

@app.route('/v1/neuralnet/evaluate',methods=['POST'])
def neural_net_v1():
    m = neural_net_messages_pb2.NeuralNetInput()
    m.ParseFromString(request.get_data())
    print m.net_id

    out = neural_net_messages_pb2.NeuralNetOutput()
    
    return out.SerializeToString(),200, {'Content-Type': 'application/octet-stream; charset=utf-8'}

def main():
    config = ConfigParser()

    #read config file
    f = open(config_file_name)
    config.readfp(f)
    f.close()

    logging.basicConfig(level=logging.INFO,format='%(levelname)s %(asctime)s %(message)s', datefmt='%m/%d/%Y %I:%M:%S %p')

    host = config.get(config_section_server,host_key)
    port = config.get(config_section_server,port_key)

    app.run(host=host,port=port)

if __name__ == '__main__':
    main()
