from flask_restful import fields

customer_fields = {
    'Cust_id': fields.Integer,
    'Cust_Created': fields.DateTime,
    'Cust_First_Name': fields.String,
    'Cust_Last_Name': fields.String,
    'Cust_Address_Name': fields.String,
    'Cust_Email': fields.String,
    'Cust_Contact_Num': fields.String,
    'Cust_Contact_Num_2': fields.String,
    'Cust_Birth_Date': fields.DateTime,
}

devices_fields = {
    'Dev_id': fields.Integer,
    'Dev_Created': fields.DateTime,
    'Dev_Manufacturer': fields.String,
    'Dev_Model': fields.String,
    'Dev_Model_Year': fields.String,
    'Dev_Identifier_Code': fields.String,
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

issues_fields = {
    'Issue_id': fields.Integer,
    'Dev_id': fields.Integer,
    'Cust_id': fields.Integer,
    'state': fields.String(attribute='status_name.Stat_Name'),
    'Prob_id': fields.Integer, # Fix typo
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

problem_fields = {
    'Prob_id': fields.Integer,
    'Prob_Name': fields.String,
    'Prob_Desc': fields.String
}

role_fields = {
    'Role_id': fields.Integer,
    'Role_Name': fields.String,
    'Role_Description': fields.String,
}

state_fields = {
    'Stat_Name': fields.String,
    'Stat_Desc': fields.String,
}

timeline_fields = {
    'Issue_id': fields.Integer,
    'Emp_id': fields.Integer,
    'Act_id': fields.Integer,
    'Ist_Comment': fields.String,
    'Ist_Created': fields.DateTime
}