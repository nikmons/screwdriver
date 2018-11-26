from api import db
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
    child = relationship('Emp_Roles') #1

    def __init__(self, Role_id, Role_Name, Role_Description):
        self.Role_id = Role_id
        self.Role_Name = Role_Name
        self.Role_Description = Role_Description

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
    Emp_Contact_Num = Column(Integer)
    Emp_Contact_Num2 = Column(Integer)
    Emp_Username = Column(String)
    Emp_Password = Column(String)
    child_Emp_Roles = relationship('Emp_Roles') #2
    child_Emp_logins = relationship('Employees_Logins') #3
    child_Issue_Timeline = relationship('Issue_Timeline') #4

class Emp_Roles(db.Model):
    __tablename__ ='Emp_Roles'
    Emp_Role_id = Column(Integer,primary_key = True)
    Emp_id = Column(Integer, ForeignKey('Employees.Emp_id')) #2
    Role_id = Column(Integer, ForeignKey('Roles.Role_id')) #1

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
    Dev_Creator = Column(Date)
    Dev_Manufacturer = Column(String)
    Dev_Model = Column (String)
    Dev_Model_Year = Column(Date)
    Dev_Identifier_Code = Column(String)  # Integer at db schema
    child_Issues = relationship('Issues') #5

class Customers (db.Model):
    __tablename__ = 'Customers'
    Cust_id = Column(Integer, primary_key = True)
    Cust_Created = Column(DateTime)
    Cust_First_Name = Column(String)
    Cust_Last_Name = Column(String)
    Cust_Address_Name = Column(String) #Also Cust_Address_Num (nomizw einai to idio me na to grapsoume sto address name)
    Cust_Email = Column(String) #Den kserw pws leme oti mporei na einai null
    Cust_Contact_Num = Column(String) #tha elega string afou den xreiazetai na kanoume praxeis me to noumero
    Cust_Contact_Num = Column(String)
    Cust_Birth_Date = Column(Date)
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
    child_Issues = relationship('Issues') #10
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
    Issue_id = Column (Integer)
    Emp_id = Column (Integer, ForeignKey('Employees.Emp_id')) #4
    Act_id = Column (Integer, ForeignKey("Action.Act_id")) #13
    Ist_Created = Column (Date)
    Ist_Comment = Column (String)

class Issues (db.Model):
     __tablename__ = 'Issues'
     Issue_id = Column(Integer, primary_key = True)
     Dev_id = Column(Integer, ForeignKey("Devices.Dev_id")) #5
     Cust_id = Column(Integer, ForeignKey("Customers.Cust_id")) #6
     Stat_id = Column(Integer, ForeignKey("State.Stat_id"))  # ____12____ Mipws auto prepei na paei ston issue timeline?
     Prob_id = Column(Integer, ForeignKey("Problems.Prob_id")) #11
     Act_id = Column(Integer, ForeignKey("Action.Act_id")) #10
     Issue_Created = Column(Date)
     Issue_Closed = Column(Date)
#______________________R  E  D________________________________________>


























