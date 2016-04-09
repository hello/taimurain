#!/usr/bin/python
from flask import Flask,url_for,request
from ConfigParser import ConfigParser
import logging
from keras.models import model_from_json
import neural_net_messages_pb2
import models

config_section_server = 'server'
config_file_name = 'config.txt'
host_key = 'host-ip'
port_key = 'port'

config_section_s3 = 's3'
bucket_key = 'data-bucket'

#yay global variables -- single threaded app, so no worries
g_keras_models = {}

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
    routes = 'ROUTES:\n'.join(list_routes()) + '\n'
    nets = 'NEURAL_NETS:\n'.join(g_keras_models.keys()) + '\n'
    return routes + nets

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
    bucket = config.get(config_section_s3,bucket_key)
    g_keras_models = models.get_models_from_s3(bucket)
    logging.info('have %d models' % len(g_keras_models))

    app.run(host=host,port=port)
    

if __name__ == '__main__':
    main()
