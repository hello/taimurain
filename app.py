#!/usr/bin/python
from flask import Flask,url_for,request
from ConfigParser import ConfigParser
import logging
from keras.models import model_from_json
import neural_net_messages_pb2
import models
import proto_utils
import json
import urllib
import sys

config_section_server = 'server'
config_file_name = sys.argv[1]
host_key = 'host-ip'
port_key = 'port'
debug_key = 'debug'

config_section_s3 = 's3'
bucket_key = 'data-bucket'

#yay global variables -- single threaded app, so no worries
g_keras_models = {}

app = Flask(__name__)

def list_routes():

    output = []
    for rule in app.url_map.iter_rules():
        methods = ','.join(rule.methods)
        line = urllib.unquote("{:50s} {:20s} {}".format(rule.endpoint, methods, rule))
        output.append(line)

    return sorted(output)

@app.route('/')
def status():
    routes = 'ROUTES:\n'.join(list_routes()) + '\n'
    return routes

@app.route('/v1/neuralnet/getavailable')
def get_available_nets():
    global g_keras_models
    return json.dumps(g_keras_models.keys())

@app.route('/v1/neuralnet/evaluate',methods=['POST'])
def neural_net_v1():
    global g_keras_models

    m = neural_net_messages_pb2.NeuralNetInput()
    m.ParseFromString(request.get_data())

    if m.net_id not in g_keras_models.keys():
        logging.warn('action=return_error reason=net_not_found net_id=%s' % m.net_id)
        return '',400

    model = g_keras_models[m.net_id]

    input_shape = model.inputs['input'].input_shape

    x = proto_utils.input_to_numpy_mat(m)
    xx = x.reshape((1,x.shape[0],x.shape[1])) #expects batch data, well a batch of one is still a batch
    
    p = model.predict({'input' : xx})

    y = p['output'][0]

    out = proto_utils.numpy_mat_to_output_proto(y)
    
    return out.SerializeToString(),200, {'Content-Type': 'application/octet-stream; charset=utf-8'}

def main():
    global g_keras_models

    config = ConfigParser()

    #read config file
    f = open(config_file_name)
    config.readfp(f)
    f.close()

    debug = config.get(config_section_server,debug_key)
    host = config.get(config_section_server,host_key)
    port = config.get(config_section_server,port_key)
    bucket = config.get(config_section_s3,bucket_key)

    log_level = logging.INFO

    if debug.upper() == 'TRUE':
        log_level = logging.DEBUG
        
    logging.basicConfig(level=log_level,format='%(levelname)s %(asctime)s %(message)s', datefmt='%m/%d/%Y %I:%M:%S %p')

    
    g_keras_models = models.get_models_from_s3(bucket)
    logging.info('action=load_models_complete num_models=%d' % len(g_keras_models))

    app.run(host=host,port=port)
    

if __name__ == '__main__':
    main()
