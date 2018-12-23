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
    'Issue_Closed': fields.DateTime
}

class IssueListAPI(Resource):

    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('Dev_id', type=int, default=0,
                                    location='json')
        self.reqparse.add_argument('Cust_id', type=int, default=0,
                                   location='json')
        self.reqparse.add_argument('Prob_id', type=int, default=0,
                                   location='json')
        # self.reqparse.add_argument('Emp_Id', type=int, default=0,
        #                            location='json')

        super(IssueListAPI, self).__init__()

    @jwt_required
    @swag_from("apidocs/issues_get.yml")
    def get(self):
        issues = models.Issues.query.all() #Query database
        print(issues)
        return {'Issues': [marshal(issue, issues_fields) for issue in issues]}

    @jwt_required
    @swag_from("apidocs/issues_post.yml")
    def post(self):
        args = self.reqparse.parse_args()
        print(args)
        issue = models.Issues(Dev_id=args["Dev_id"],
                                    Cust_id=args["Cust_id"], Prob_id=args["Prob_id"])
        db.session.add(issue)
        db.session.commit()
