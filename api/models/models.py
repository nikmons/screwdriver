import datetime

from app import db
from sqlalchemy.dialects.postgresql import JSON
from sqlalchemy.orm import relationship
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Table, Column, Integer, ForeignKey, String, Date, DateTime, Boolean

#__________________________B  L  U  E________________________________>

class Roles(db.Model):
    __tablename__ = 'Roles'
    Role_id = Column(Integer, primary_key = True)
    Role_Name = Column(String)
    Role_Description = Column(String)
    child_role = relationship('Emp_Roles', backref='emp_rol') #1

    def __repr__(self):
        return '<Role_id {}>'.format(self.Role_id)

class Employees(db.Model):
    __tablename__ = 'Employees'
    Emp_id = Column(Integer, primary_key = True)
    Emp_Created = Column(DateTime)
    Emp_First_Name = Column(String)
    Emp_Last_Name = Column(String)
    Emp_Address_Name = Column(String)
    Emp_Address_Num = Column(Integer)
    Emp_Email = Column(String)
    Emp_Contact_Num = Column(String) # Validate
    Emp_Contact_Num2 = Column(String) # Validate
    Emp_Username = Column(String)
    Emp_Password = Column(String)
    #child_Emp_Roles = relationship('Emp_Roles') #2
    #child_Emp_logins = relationship('Employees_Logins') #3
    #child_Issue_Timeline = relationship('Issue_Timeline') #4

    # TODO: Implement
    def is_authenticated(self):
        return True
 
    # TODO: Implement
    def is_active(self):
        return True
 
    # TODO: Implement
    def is_anonymous(self):
        return False
 
    def get_id(self):
        return self.Emp_id

class Emp_Roles(db.Model):
    __tablename__ ='Emp_Roles'
    Emp_Role_id = Column(Integer,primary_key = True)
    Emp_id = Column(Integer, ForeignKey('Employees.Emp_id')) #2
    Role_id = Column(Integer, ForeignKey('Roles.Role_id')) #1

    master_role = relationship('Roles', backref='role_info', foreign_keys=[Role_id]) #1

class Employees_Logins(db.Model):
    __tablename__ = 'Employees_logins'
    Emp_Logins_id = Column(Integer, primary_key=True)
    Emp_id = Column(Integer, ForeignKey ("Employees.Emp_id")) #3
    Emp_Logged_In = Column (Date) #timestamp
    Emp_Logged_Out = Column (Date) #timestamp

#_________________________B  L  U  E_________________________________>

#_________________________G  R  E  E  N______________________________>

class Devices (db.Model):
    __tablename__ = 'Devices'
    Dev_id = Column(Integer, primary_key = True)
    Dev_Created = Column(DateTime, default=datetime.datetime.utcnow)
    Dev_Manufacturer = Column(String)
    Dev_Model = Column (String)
    Dev_Model_Year = Column(String)
    Dev_Identifier_Code = Column(String)  # Integer at db schema
    child_Issues = relationship('Issues') #5

class Customers (db.Model):
    __tablename__ = 'Customers'
    Cust_id = Column(Integer, primary_key = True)
    Cust_Created = Column(DateTime, default=datetime.datetime.utcnow)
    Cust_First_Name = Column(String)
    Cust_Last_Name = Column(String)
    Cust_Address_Name = Column(String) #Also Cust_Address_Num (nomizw einai to idio me na to grapsoume sto address name)
    Cust_Email = Column(String) #Den kserw pws leme oti mporei na einai null
    Cust_Contact_Num = Column(String) # Validate
    Cust_Contact_Num_2 = Column(String) # Validate
    Cust_Birth_Date = Column(Date, default=datetime.datetime.utcnow)
    child_Issues = relationship('Issues') #6


#______________________G  R  E  E  N__________________________________>

#______________________R  E  D________________________________________>

class Inventory (db.Model):
    __tablename__ = 'Inventory'
    Inv_id = Column(Integer, primary_key = True)
    Inv_Item_Name = Column(String) #Integer at db schema
    Inv_Item_Desc = Column(String) #Integer at db schema
    Inv_Item_Price = Column(Integer) #prepei na to kanoume float
    child_Act_Inv = relationship('Action_Inventory') #7

class Action_Inventory (db.Model):
    __tablename__ = 'Action_Inventory'
    Act_Inv_id = Column(Integer, primary_key = True)
    Act_id = Column (Integer, ForeignKey("Action.Act_id")) #7
    Inv_id = Column (Integer, ForeignKey(Inventory.Inv_id)) #9
    Inv_Quantity = Column (Integer)

class Action (db.Model):
    __tablename__ = 'Action'
    Act_id = Column(Integer, primary_key = True)
    Act_id_to = Column(Integer, ForeignKey("Action.Act_id")) #8
    Act_Is_Final = Column(Boolean)
    Act_Name = Column(String)
    Act_Desc = Column(String)
    child_Action = relationship('Action') #8
    child_Act_Inv = relationship('Action_Inventory') #9
    child_Issues = relationship('Issue_Timeline') #13

class Problems (db.Model):
    __tablename__ = 'Problems'
    Prob_id = Column(Integer, primary_key = True)
    Prob_Name = Column(String)
    Prob_Desc = Column(String)
    child_Issues = relationship('Issues') #11

class State (db.Model):
    __tablename__ = 'State'
    Stat_id = Column(Integer, primary_key = True)
    Stat_Name = Column(String)
    Stat_Desc = Column(String)
    child_Issues = relationship('Issues') #12

class Issue_Timeline (db.Model):
    __tablename__ = 'Issue_Timeline'
    Ist_id = Column(Integer, primary_key = True)
    Issue_id = Column (Integer, ForeignKey("Issues.Issue_id"))
    Emp_id = Column (Integer, ForeignKey('Employees.Emp_id')) #4
    Act_id = Column (Integer, ForeignKey("Action.Act_id")) #13
    Ist_Created = Column (DateTime)
    Ist_Comment = Column (String)

class Issues (db.Model):
     __tablename__ = 'Issues'
     Issue_id = Column(Integer, primary_key = True)
     Dev_id = Column(Integer, ForeignKey("Devices.Dev_id")) #5
     Cust_id = Column(Integer, ForeignKey("Customers.Cust_id")) #6
     Stat_id = Column(Integer, ForeignKey("State.Stat_id"), default=1)  # ____12____ Mipws auto prepei na paei ston issue timeline?
     Prob_id = Column(Integer, ForeignKey("Problems.Prob_id")) #11
     Issue_Created = Column(DateTime, default=datetime.datetime.utcnow)
     Issue_Closed = Column(Date)
     child_Issues = relationship("Issue_Timeline")
#______________________R  E  D________________________________________>
