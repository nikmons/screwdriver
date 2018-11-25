#!flask/bin/python

"""Alternative version of the ToDo RESTful server implemented using the
Flask-RESTful extension."""

from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal
from flask_httpauth import HTTPBasicAuth
from flask_sqlalchemy import SQLAlchemy
from datetime import datetime

import os

app = Flask(__name__, static_url_path="")
app.config["SQLALCHEMY_DATABASE_URI"] = os.environ['DATABASE_URL']

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


class Employees(Resource):
    decorators = [auth.login_required]

    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('emp_created', type=date(), required=True, help='No task title provided',
                                   location='json')
        self.reqparse.add_argument('emp_first_name', type=str, required=True, help='No first_name provided',
                                   location='json')
        self.reqparse.add_argument('emp_last_name', type=str, required=True, help='No last_name provided',
                                   location='json')
        self.reqparse.add_argument('emp_address_name', type=str, required=True, help='No task title provided',
                                   location='json')
        self.reqparse.add_argument('emp_address_num', type=int, required=True, help='No task title provided',
                                   location='json')
        self.reqparse.add_argument('emp_email', type=str, required=True, help='No task title provided',
                                   location='json')
        self.reqparse.add_argument('emp_contact_num', type=int, required=True, help='No task title provided',
                                   location='json')
        self.reqparse.add_argument('emp_contact_num2', type=int, required=True, help='No task title provided',
                                   location='json')
        self.reqparse.add_argument('emp_username', type=str, required=True, help='No task title provided',
                                   location='json')
        self.reqparse.add_argument('emp_password', type=str, required=True, help='No task title provided',
                                   location='json')
        super(Employees, self).__init__()

    def get(self):
        return {'tasks': [marshal(task, task_fields) for task in tasks]}

    def post(self):
        args = self.reqparse.parse_args()
        emp = {
            'Emp_Created': args['emp_created'],
            'Emp_First_Name': args['emp_first_name'],
            'Emp_Last_Name': args['last_name'],
            'Emp_Address_Name': args['address_name'],
            'Emp_Address_Num': args['address_num'],
            'Emp_Email': args['emp_email'],
            'Emp_Contact_Num': args['emp_contact_num'],
            'Emp_Contact_Num2': args['emp_contact_num2'],
            'Emp_Username': args['emp_username'],
            'Emp_Password': args['emp_password']
        }
        try:
            db.session.add(emp)
            return "done"
        except:
            return "Not done"

class TaskAPI(Resource):
    decorators = [auth.login_required]

    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('title', type=str, location='json')
        self.reqparse.add_argument('description', type=str, location='json')
        self.reqparse.add_argument('done', type=bool, location='json')
        super(TaskAPI, self).__init__()

    def get(self, id):
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


api.add_resource(Employee, '/api/employee', endpoint='Employee')
api.add_resource(TaskAPI, '/todo/api/v1.0/tasks/<int:id>', endpoint='task')


if __name__ == '__main__':
    app.run(debug=True)
