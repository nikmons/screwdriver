from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal
from flasgger import swag_from
from flask_jwt_extended import jwt_required, get_jwt_identity

import datetime

from app import db
from models import models

issues_fields = {
    'Issue_id': fields.Integer,
    'Dev_id': fields.Integer,
    'Cust_id': fields.Integer,
    'Stat_id': fields.Integer,
    'Prob_id': fields.Integer, # Fix typo
    'Issue_Created': fields.DateTime,
    'Issue_Closed': fields.DateTime,
    'Issue_Created_By': fields.Integer,
    'Issue_Assigned_To': fields.Integer    
}

class MyIssuesListAPI(Resource):

    def __init__(self):
        super(MyIssuesListAPI, self).__init__()

    @jwt_required
    @swag_from("apidocs/user_issues_get.yml")
    def get(self):
        emp_id = get_jwt_identity()
        issues = models.Issues.query.filter_by(Issue_Assigned_To=emp_id) #Query database
        #print(issues)
        issues_json = {'Issues': [marshal(issue, issues_fields) for issue in issues]}
        print(issues_json)
        return issues_json
