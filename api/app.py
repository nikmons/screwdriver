#!api/api.py
import os
import datetime
import flask_login

from flask import Flask, jsonify, abort, make_response#, session
from flask_restful import Api, Resource, reqparse, fields, marshal
from flask_httpauth import HTTPBasicAuth
from flasgger import Swagger, swag_from
from flask_sqlalchemy import SQLAlchemy
from dotenv import load_dotenv

load_dotenv(verbose=True)

app = Flask(__name__, static_url_path="")
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = os.getenv("SQLALCHEMY_TRACK_MODIFICATIONS")
app.config["SQLALCHEMY_DATABASE_URI"] = os.getenv("DATABASE_URL")
app.config["ENVIRONMENT"] = os.getenv("ENV")
app.config["CSRF_ENABLED"] = True
app.secret_key = "prepei na broume kati gi auto edw to kleidi" # TODO: handle secret key
#print(os.getenv("ENV"))
#print(os.getenv("DATABASE_URL"))

swagger_template = {'securityDefinitions': { 'basicAuth': { 'type': 'basic' } }}

swagger = Swagger(app, template=swagger_template)
api = Api(app)
auth = HTTPBasicAuth()
db =  SQLAlchemy(app)
login_manager = flask_login.LoginManager()
login_manager.init_app(app)

from resources.employee_list import EmployeeListAPI
from resources.employee import EmployeeAPI
from resources.device_list import DeviceListAPI
from resources.device import DeviceAPI
from resources.customer_list import CustomerListAPI
from resources.customer import CustomerAPI
from resources.login import LoginAPI
from resources.logout import LogoutAPI

import models

"""
@auth.get_password
def get_password(username):
    if username == 'admin':
        return 'admin'
    return None
"""

@login_manager.user_loader
def load_user(id):
    print("Load User: {}".format(id))
    return models.models.Employees.query.get(int(id))

@auth.error_handler
def unauthorized():
    # return 403 instead of 401 to prevent browsers from displaying the default
    # auth dialog
    return make_response(jsonify({'message': 'Unauthorized access'}), 403)

api.add_resource(LoginAPI, '/todo/api/v1.0/login', endpoint='login')
api.add_resource(LogoutAPI, '/todo/api/v1.0/logout', endpoint='logout')
api.add_resource(DeviceListAPI, '/todo/api/v1.0/devices', endpoint='devices')
api.add_resource(DeviceAPI, '/todo/api/v1.0/devices/<int:id>', endpoint='device')
api.add_resource(CustomerListAPI, '/todo/api/v1.0/customers', endpoint='customers')
api.add_resource(CustomerAPI, '/todo/api/v1.0/customers/<int:id>', endpoint='customer')
api.add_resource(EmployeeListAPI, '/todo/api/v1.0/employees', endpoint='employees')
api.add_resource(EmployeeAPI, '/todo/api/v1.0/employees/<int:id>', endpoint='employee')

if __name__ == '__main__':
    app.run(debug=True)
