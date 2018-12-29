#!api/api.py
import os
import datetime

from flask import Flask, jsonify, abort, make_response#, session
from flask_restful import Api, Resource, reqparse, fields, marshal
from flask_jwt_extended import (
    JWTManager, jwt_required, create_access_token,
    get_jwt_identity, create_refresh_token,
    jwt_refresh_token_required, get_raw_jwt
)
from flasgger import Swagger, swag_from
from flask_sqlalchemy import SQLAlchemy
from dotenv import load_dotenv

load_dotenv(verbose=True)

app = Flask(__name__, static_url_path="")
app.secret_key = os.getenv("FLASK_SECRET_KEY") # Load from env var
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = os.getenv("SQLALCHEMY_TRACK_MODIFICATIONS")
app.config["SQLALCHEMY_DATABASE_URI"] = os.getenv("DATABASE_URL")
app.config["ENVIRONMENT"] = os.getenv("ENV")
app.config["CSRF_ENABLED"] = True
app.config["SWAGGER"] = {"title":"Swagger JWT Authentication App", "uiversion":3}
app.config['JWT_BLACKLIST_ENABLED'] = True
app.config['JWT_BLACKLIST_TOKEN_CHECKS'] = ['access', 'refresh']
app.config['PROPAGATE_EXCEPTIONS'] = True

swagger_template={
        "openapi": "2.0.0",
        "info": {
            "title": "Scewdriver API (JWT Auth)",
            "version": "1.0",
        },
        "securityDefinitions": {
            "Bearer":{
                "type": "apiKey",
                "name": "Authorization",
                "in": "header"
            }
        },
        "produces": [
            "application/json",
        ],
        "security": [
            {"Bearer": "[]"}
        ]
    }

swagger = Swagger(app, template=swagger_template)#, template=swagger_template)
api = Api(app)
db =  SQLAlchemy(app)
jwt = JWTManager(app)

blacklist = set()

@jwt.token_in_blacklist_loader
def check_if_token_in_blacklist(decrypted_token):
    jti = decrypted_token['jti']
    return jti in blacklist

from resources.employee_list import EmployeeListAPI
from resources.employee import EmployeeAPI
from resources.device_list import DeviceListAPI
from resources.device import DeviceAPI
from resources.customer_list import CustomerListAPI
from resources.customer import CustomerAPI
from resources.login import LoginAPI
from resources.logout import LogoutAPI
from resources.problems_list import ProblemListAPI
from resources.states_list import StatesListAPI
from resources.issue_list import IssueListAPI
from resources.role import RoleAPI
from resources.role_list import RoleListAPI
from resources.employee_roles import EmployeeRolesAPI

import models

api.add_resource(LoginAPI, '/todo/api/v1.0/login', endpoint='login')
api.add_resource(LogoutAPI, '/todo/api/v1.0/logout', endpoint='logout')
api.add_resource(DeviceListAPI, '/todo/api/v1.0/devices', endpoint='devices')
api.add_resource(DeviceAPI, '/todo/api/v1.0/devices/<int:id>', endpoint='device')
api.add_resource(CustomerListAPI, '/todo/api/v1.0/customers', endpoint='customers')
api.add_resource(CustomerAPI, '/todo/api/v1.0/customers/<int:id>', endpoint='customer')
api.add_resource(EmployeeListAPI, '/todo/api/v1.0/employees', endpoint='employees')
api.add_resource(EmployeeAPI, '/todo/api/v1.0/employees/<int:id>', endpoint='employee')
api.add_resource(ProblemListAPI, '/todo/api/v1.0/problems', endpoint='problems')
api.add_resource(StatesListAPI, '/todo/api/v1.0/states', endpoint='states')
api.add_resource(IssueListAPI, '/todo/api/v1.0/issues', endpoint='issues')
api.add_resource(RoleListAPI, '/todo/api/v1.0/roles', endpoint='roles')
api.add_resource(RoleAPI, '/todo/api/v1.0/roles/<int:id>', endpoint='role')
api.add_resource(EmployeeRolesAPI, '/todo/api/v1.0/employees/<int:id>/roles', endpoint='employee_roles')

if __name__ == '__main__':
    app.run(debug=True)
