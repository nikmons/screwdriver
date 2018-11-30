"""empty message

Revision ID: 6ced42f5ecf2
Revises: d590ad313a55
Create Date: 2018-11-30 22:20:00.827382

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '6ced42f5ecf2'
down_revision = 'd590ad313a55'
branch_labels = None
depends_on = None


def upgrade():
	op.execute('ALTER TABLE "Employees" ALTER COLUMN "Emp_Contact_Num" TYPE character varying COLLATE pg_catalog."default"')
	op.execute('ALTER TABLE "Employees" ALTER COLUMN "Emp_Contact_Num2" TYPE character varying COLLATE pg_catalog."default"')

def downgrade():
	#op.execute('ALTER TABLE "Employees" ALTER COLUMN "Emp_Contact_Num" TYPE integer')
	#op.execute('ALTER TABLE "Employees" ALTER COLUMN "Emp_Contact_Num2" TYPE integer')
	pass