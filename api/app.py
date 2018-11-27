#!api/api.py
from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal
from flask_httpauth import HTTPBasicAuth
from flasgger import Swagger, swag_from
from flask_sqlalchemy import SQLAlchemy
from dotenv import load_dotenv

import os
import datetime

load_dotenv(verbose=True)

app = Flask(__name__, static_url_path="")
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = os.getenv("SQLALCHEMY_TRACK_MODIFICATIONS")
app.config["SQLALCHEMY_DATABASE_URI"] = os.getenv("DATABASE_URL")
app.config["ENVIRONMENT"] = os.getenv("ENV")
app.config["CSRF_ENABLED"] = True

#print(os.getenv("ENV"))
#print(os.getenv("DATABASE_URL"))

swagger_template = {'securityDefinitions': { 'basicAuth': { 'type': 'basic' } }}

swagger = Swagger(app, template=swagger_template)
flask_api = Api(app)
auth = HTTPBasicAuth()
db =  SQLAlchemy(app)

from resources.employee_list import EmployeeListAPI
from resources.employee import EmployeeAPI
import models

@auth.get_password
def get_password(username):
    if username == 'admin':
        return 'admin'
    return None


@auth.error_handler
def unauthorized():
    # return 403 instead of 401 to prevent browsers from displaying the default
    # auth dialog
    return make_response(jsonify({'message': 'Unauthorized access'}), 403)

flask_api.add_resource(EmployeeListAPI, '/todo/api/v1.0/employees', endpoint='employees')
flask_api.add_resource(EmployeeAPI, '/todo/api/v1.0/employees/<int:id>', endpoint='employee')
#api.add_resource(TaskListAPI, '/todo/api/v1.0/tasks', endpoint='tasks')
#api.add_resource(TaskAPI, '/todo/api/v1.0/tasks/<int:id>', endpoint='task')

if __name__ == '__main__':
    app.run(debug=True)
