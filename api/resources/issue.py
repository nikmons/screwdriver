from flask import Flask, jsonify, abort, make_response, jsonify
from flask_restful import Api, Resource, reqparse, fields, marshal
from flasgger import swag_from
from flask_jwt_extended import jwt_required, get_jwt_identity

from app import db
from models import models

issue_fields = {
    'Issue_id': fields.Integer,
    'Dev_id': fields.Integer,
    'Cust_id': fields.Integer,
    'Stat_id': fields.Integer,
    'Prob_id': fields.Integer,
    'Issue_Created': fields.DateTime,
    'Issue_Closed': fields.DateTime,
    'Issue_Created_By': fields.Integer,
    'Issue_Assigned_To': fields.Integer,
    'Issue_Track_Num': fields.String,
    'Issue_Delivery_At': fields.String,
}

action_fields = {
    'Act_id' : fields.Integer,
    'Act_Name': fields.String,
    'Act_Desc': fields.String
}


class IssueAPI(Resource):

    def __init__(self):
        super(IssueAPI, self).__init__()

    @jwt_required
    @swag_from("apidocs/issue_put.yml")
    def put(self, id):
        print("Update id = {}".format(id))

    @jwt_required
    @swag_from("apidocs/issue_get.yml")
    def get(self, id):
        print("Get id = {}".format(id))
        issue = models.Issues.query.filter_by(Issue_id=id).first()
        last_act_id = models.Issue_Timeline.query.filter_by(Issue_id=id).order_by(models.Issue_Timeline.Ist_id.desc()).first().Act_id
        av_actions = models.Action_Stmdl.query.filter_by(Act_id_from=last_act_id).all()
        print(av_actions)        

        print(issue)
        resp = marshal(issue, issue_fields) #,200
        resp["Available_actions"] = [marshal(av_act.transition_to, action_fields) for av_act in av_actions]
        print(resp)
        return resp
        
