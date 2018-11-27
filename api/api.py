#!api/api.py
from flask import Flask, jsonify, abort, make_response,session
from flask_restful import Api, Resource, reqparse, fields, marshal
from flask_httpauth import HTTPBasicAuth
from flasgger import Swagger, swag_from
from flask_sqlalchemy import SQLAlchemy
from dotenv import load_dotenv

import os

load_dotenv(verbose=True)

app = Flask(__name__, static_url_path="")

app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = os.getenv("SQLALCHEMY_TRACK_MODIFICATIONS")
app.config["SQLALCHEMY_DATABASE_URI"] = os.getenv("DATABASE_URL")
app.config["ENVIRONMENT"] = os.getenv("ENV")
app.config["CSRF_ENABLED"] = True
app.secret_key = "prepei na broume kati gi auto edw to kleidi"
#print(os.getenv("ENV"))
#print(os.getenv("DATABASE_URL"))

swagger_template = {'securityDefinitions': { 'basicAuth': { 'type': 'basic' } }}

swagger = Swagger(app, template=swagger_template)
api = Api(app)
auth = HTTPBasicAuth()
db =  SQLAlchemy(app)
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

"""Task Array"""
tasks = [
    {
        'id': 1,
        'title': u'Buy groceries',
        'description': u'Milk, Cheese, Pizza, Fruit, Tylenol',
        'done': False
    },
    {
        'id': 2,
        'title': u'Learn Python',
        'description': u'Need to find a good Python tutorial on the web',
        'done': False
    }
]

task_fields = {
    'title': fields.String,
    'description': fields.String,
    'done': fields.Boolean,
    'uri': fields.Url('task')
}

employee_fields = {
    'Emp_id': fields.Integer,
    'Emp_Created': fields.DateTime,
    'Emp_First_Name': fields.String,
    'Emp_Last_Name': fields.String,
    'Emp_Address_Name': fields.String,
    'Emp_Address_Num': fields.Integer,
    'Emp_Email': fields.String,
    'Emp_Contact_Num': fields.Integer,
    'Emp_Contact_Num2': fields.Integer,
    'Emp_Username': fields.String,
    'Emp_Password': fields.String
}

class EmployeeListAPI(Resource):
    decorators = [auth.login_required]

    def __init__(self):
        super(EmployeeListAPI, self).__init__()

    def get(self):
        """
        file: apidocs/employees_get.yml
        """
        employees = models.Employees.query.all() #Query database
        print(employees)
        return {'employees': [marshal(employee, employee_fields) for employee in employees]}

    def post(self):
        print(1)

class TaskListAPI(Resource):
    decorators = [auth.login_required]

    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('title', type=str, required=True,
                                   help='No task title provided',
                                   location='json')
        self.reqparse.add_argument('description', type=str, default="",
                                   location='json')
        super(TaskListAPI, self).__init__()

    def get(self):
        """
        file: apidocs/tasks_get.yml
        """
        return {'tasks': [marshal(task, task_fields) for task in tasks]}

    def post(self):
        args = self.reqparse.parse_args()
        task = {
            'id': tasks[-1]['id'] + 1,
            'title': args['title'],
            'description': args['description'],
            'done': False
        }
        tasks.append(task)
        return {'task': marshal(task, task_fields)}, 201


class TaskAPI(Resource):
    decorators = [auth.login_required]

    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('title', type=str, location='json')
        self.reqparse.add_argument('description', type=str, location='json')
        self.reqparse.add_argument('done', type=bool, location='json')
        super(TaskAPI, self).__init__()

    def get(self, id):
        """
        file: apidocs/task_get.yml
        """
        task = [task for task in tasks if task['id'] == id]
        if len(task) == 0:
            abort(404)
        return {'task': marshal(task[0], task_fields)}

    def put(self, id):
        task = [task for task in tasks if task['id'] == id]
        if len(task) == 0:
            abort(404)
        task = task[0]
        args = self.reqparse.parse_args()
        for k, v in args.items():
            if v is not None:
                task[k] = v
        return {'task': marshal(task, task_fields)}

    def delete(self, id):
        task = [task for task in tasks if task['id'] == id]
        if len(task) == 0:
            abort(404)
        tasks.remove(task[0])
        return {'result': True}

class LoginAPI(Resource):
    decorators = [auth.login_required]
    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('username', required = True, type=str, location='json')
        self.reqparse.add_argument('password', required = True, type=str, location='json')
        super(TaskAPI, self).__init__()


    def post(self):
        args = self.reqparse.parse_args()
        newUser = models.Employees.query.filter_by(Emp_Username = args['username']).first()
        if newUser:
            if newUser.Emp_Password == args['password']:
                if username == args['username'] for username in session['Users']:
                    return "logged"
                else:
                    session['Users'].append(args['username'])
            else:
                return "denied"
        else:
            return "not found"

class LogoutAPI(Resource):
    decorators = [auth.login_required]
    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('username', required = True, type=str, location='json')
        super(TaskAPI, self).__init__()


    def post(self):
        args = self.reqparse.parse_args()
        for username in session['Users']:
            if username == args['username']:
                session.pop(args['username'], None)
                return "logged out"
        return "cant log out"

api.add_resource(LoginAPI, '/todo/api/v1.0/login', endpoint='login')
api.add_resource(LogoutAPI, '/todo/api/v1.0/logout', endpoint='logout')

api.add_resource(EmployeeListAPI, '/todo/api/v1.0/employees', endpoint='employees')
api.add_resource(TaskListAPI, '/todo/api/v1.0/tasks', endpoint='tasks')
api.add_resource(TaskAPI, '/todo/api/v1.0/tasks/<int:id>', endpoint='task')


if __name__ == '__main__':
    app.run(debug=True)
