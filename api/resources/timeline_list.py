import datetime

from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal
from flasgger import swag_from
from flask_jwt_extended import jwt_required, get_jwt_identity

from app import db
from models import models

timeline_fields = {
    'Issue_id': fields.Integer,
    'Emp_id': fields.Integer,
    'Act_id': fields.Integer,
    'Ist_Comment': fields.String,
    'Ist_Created': fields.DateTime
}

class IssueTimelineAPI(Resource):

    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('Act_id', type=int, default=1,
                                    location='json')
        self.reqparse.add_argument('Ist_Comment', type=str, default='Default Comment',
                                    location='json')   
        super(IssueTimelineAPI, self).__init__()


    @jwt_required
    @swag_from("apidocs/timeline_get.yml")
    def get(self, id):
        print("Get Timeline Entries for Issue = {}".format(id))

        entries = db.session.query(models.Issue_Timeline)       \
            .filter(models.Issue_Timeline.Issue_id == id)       \
            .order_by(models.Issue_Timeline.Ist_id.asc())       \
            .all()

        print(entries)
        return {'Entries': [marshal(entry, timeline_fields) for entry in entries]}
        
    @jwt_required
    @swag_from("apidocs/timeline_post.yml")
    def post(self, id):
        emp_id = get_jwt_identity()
        print("Enter new Timeline Entry for Issue = {}".format(id))
        args = self.reqparse.parse_args()
        print(args)

        issue = models.Issues.query.filter_by(Issue_id=id).first()
        if issue is None:
            return {"message" : "Issue '{}' does not exist".format(id)}, 400

        action_added = models.Action.query.filter_by(Act_id=args["Act_id"]).first()
        if action_added is None:
            return {"message" : "Action '{}' does not exist".format(args["Act_id"])}, 400

        timeline_entry = models.Issue_Timeline(Issue_id=id,                         \
            Emp_id=emp_id, Act_id=args["Act_id"], Ist_Comment=args["Ist_Comment"])

        self.__issue_transition(issue, action_added)

        db.session.add(timeline_entry)
        db.session.commit()

    def __issue_transition(self, issue, new_action):
        if new_action.Act_Name == "Fixed":
            # Assign to QA
            print("Assign to QA")
            qa = [emp_role.master_employee for emp_role in models.Emp_Roles.query.filter_by(Role_id=5).all()]
            issue.Issue_Assigned_To = qa[0].Emp_id
        elif new_action.Act_Name == "Undamaged" or new_action.Act_Name == "Unrepairable" or new_action.Act_Name == "Tested-Fixed":
            # Assign to Helpdesk
            print("Assign to Helpdesk")
            helpdesk = [emp_role.master_employee for emp_role in models.Emp_Roles.query.filter_by(Role_id=3).all()]
            issue.Issue_Assigned_To = helpdesk[0].Emp_id

            # If assigned for Home delivery assign to Courrier   
        elif new_action.Act_Name == "Tested-Unfixed":
            # Assign to Technician for recheck
            print("Assign to Technician")
            technician = [emp_role.master_employee for emp_role in models.Emp_Roles.query.filter_by(Role_id=1).all()]
            issue.Issue_Assigned_To = technician[0].Emp_id
        elif new_action.Act_Name == "Returned":
            print("Close it")
            issue.Issue_Closed = datetime.datetime.utcnow
            issue.Issue_Assigned_To = None
        print("+++++++++++++++")
        #db.session.update(issue)
        db.session.commit()