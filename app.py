#!/usr/bin/python
from flask import Flask
from ConfigParser import ConfigParser
import logging

config_section_server = 'server'
config_file_name = 'config.txt'
host_key = 'host-ip'
port_key = 'port'

app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'Hello World!'


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
