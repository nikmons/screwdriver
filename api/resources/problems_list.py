from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal
from flasgger import swag_from
from flask_jwt_extended import jwt_required, get_jwt_identity

from app import db
from models import models

problem_fields = {
    'Prob_id': fields.Integer,
    'Prob_Name': fields.String,
    'Prob_Desc': fields.String
}

class ProblemListAPI(Resource):

    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('Prob_Id', type=int, default="",
                            location='json')
        self.reqparse.add_argument('Prob_Name', type=str, default="",
                                   location='json')
        self.reqparse.add_argument('Prob_Desc', type=str, default="",
                                    location='json')
        super(ProblemListAPI, self).__init__()

    @jwt_required
    @swag_from("apidocs/problems_get.yml")
    def get(self):
        problems = models.Problems.query.all() #Query database
        print(problems)
        return {'problems': [marshal(problem, problem_fields) for problem in problems]}

    @jwt_required
    @swag_from("apidocs/problems_post.yml")
    def post(self):
        args = self.reqparse.parse_args()
        print(args)
        problem = models.Problems(Prob_Name=args["Prob_Name"], Prob_Desc=args["Prob_Desc"])
        db.session.add(problem)
        db.session.commit()
