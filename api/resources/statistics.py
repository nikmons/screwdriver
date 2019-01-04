from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal
from flasgger import swag_from
from flask_jwt_extended import jwt_required, get_jwt_identity
from sqlalchemy.sql import func
from flask_cors import cross_origin

from app import db
from models import models

class StatisticsAPI(Resource):

    def __init__(self):
        super(StatisticsAPI, self).__init__()

    #@jwt_required
    @swag_from("apidocs/statistics_get.yml")
    def get(self):
        emps_helpdesk = self.__get_employees_by_role(3)
        emps_technician = self.__get_employees_by_role(1)
        emps_qa = self.__get_employees_by_role(5)
        emps_courrier = self.__get_employees_by_role(4)

        stats = {}
        stats["total_issues"] = models.Issues.query.count()
        stats["closed_issues"] = models.Issues.query.filter(
            models.Issues.Issue_Closed.isnot(None)).count()
        stats["avg_issue_lifetime"] = self.__td_to_std(self.__avg_issue_lifetime())
        stats["helpdesk_issues"] = self.__get_issues_by_emps(emps_helpdesk)
        stats["technician_issues"] = self.__get_issues_by_emps(emps_technician)
        stats["qa_issues"] = self.__get_issues_by_emps(emps_qa)
        stats["courrier_issues"] = self.__get_issues_by_emps(emps_courrier)
        stats["helpdesk_employees"] = len(emps_helpdesk)
        stats["technician_employees"] = len(emps_technician)
        stats["qa_employees"] = len(emps_qa)
        stats["courrier_employees"] = len(emps_courrier)
        stats["total_devices"] = models.Devices.query.count()
        stats["total_customers"] = models.Customers.query.count()
        print(stats)

        return {"stats" : stats}, 200

    def __avg_issue_lifetime(self):
        return db.session.query(                                                                \
            func.avg(models.Issues.Issue_Closed - models.Issues.Issue_Created).label("average"))\
            .filter(models.Issues.Issue_Closed.isnot(None))                                     \
            .first()

    def __td_to_std(self, td):
        # Days-Hours-Minutes
        print(td)
        if td[0] is None:
            return {"days":-1, "hours":-1, "minutes":-1}
        else:
            days = td[0].days 
            hours = td[0].seconds//3600
            minutes = (td[0].seconds//60)%60
            return {"days" : days, "hours" : hours, "minutes" : minutes}

    def __get_employees_by_role(self, role_id):
        emp_roles = models.Emp_Roles.query.filter(models.Emp_Roles.Role_id == role_id).all()
        return [emp_role.master_employee for emp_role in emp_roles]

    def __get_issues_by_emps(self, emps):
        return models.Issues.query.filter(
            models.Issues.Issue_Assigned_To.in_([emp.Emp_id for emp in emps])).count()