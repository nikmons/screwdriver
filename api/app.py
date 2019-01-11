#!api/api.py
import os
import datetime

from flask import Flask, jsonify, abort, make_response, render_template
from flask_restful import Api, Resource, reqparse, fields, marshal
from flask_jwt_extended import (
    JWTManager, jwt_required, create_access_token,
    get_jwt_identity, create_refresh_token,
    jwt_refresh_token_required, get_raw_jwt
)
from flasgger import Swagger, swag_from
from flask_sqlalchemy import SQLAlchemy
from flask_cors import CORS
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
app.config['CORS_HEADERS'] = 'Content-Type'

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
cors = CORS(app, resources={r"/api/issues/findByTrackNum" : {"origins":"*"}})

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
from resources.user_issues_list import MyIssuesListAPI
from resources.issue import IssueAPI
from resources.timeline_list import IssueTimelineAPI
from resources.issue_by_tracknum import IssueFindByTrackNumAPI
from resources.statistics import StatisticsAPI

import models

api.add_resource(LoginAPI, '/api/login', endpoint='login')
api.add_resource(LogoutAPI, '/api/logout', endpoint='logout')
api.add_resource(DeviceListAPI, '/api/devices', endpoint='devices')
api.add_resource(DeviceAPI, '/api/devices/<int:id>', endpoint='device')
api.add_resource(CustomerListAPI, '/api/customers', endpoint='customers')
api.add_resource(CustomerAPI, '/api/customers/<int:id>', endpoint='customer')
api.add_resource(EmployeeListAPI, '/api/employees', endpoint='employees')
api.add_resource(EmployeeAPI, '/api/employees/<int:id>', endpoint='employee')
api.add_resource(ProblemListAPI, '/api/problems', endpoint='problems')
api.add_resource(StatesListAPI, '/api/states', endpoint='states')
api.add_resource(IssueListAPI, '/api/issues', endpoint='issues')
api.add_resource(RoleListAPI, '/api/roles', endpoint='roles')
api.add_resource(RoleAPI, '/api/roles/<int:id>', endpoint='role')
api.add_resource(EmployeeRolesAPI, '/api/employees/<int:id>/roles', endpoint='employee_roles')
api.add_resource(MyIssuesListAPI, '/api/myissues', endpoint='user_issues')
api.add_resource(IssueAPI, '/api/issues/<int:id>', endpoint='issue')
api.add_resource(IssueTimelineAPI, '/api/myissues/<int:id>/timeline', endpoint='issue_timeline')
api.add_resource(IssueFindByTrackNumAPI, '/api/issues/findByTrackNum', endpoint='issue_bytracknum')
api.add_resource(StatisticsAPI, '/api/statistics', endpoint='statistics')

@app.route("/")
@app.route("/index")
def index():
    return render_template("index.html")

if __name__ == '__main__':
    app.run(debug=True)
