"""empty message

Revision ID: 852648571a3c
Revises: 27c544cc6a24
Create Date: 2018-12-26 12:01:58.744733

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '852648571a3c'
down_revision = '27c544cc6a24'
branch_labels = None
depends_on = None


def upgrade():
    op.drop_column('Employees', 'Emp_Created')
    op.drop_column('Employees_logins', 'Emp_Logged_In')
    op.drop_column('Employees_logins', 'Emp_Logged_Out')
    op.drop_column('Issue_Timeline', 'Ist_Created')
    op.add_column('Employees', sa.Column('Emp_Created', sa.DateTime(), nullable=True))
    op.add_column('Employees_logins', sa.Column('Emp_Logged_In', sa.DateTime(), nullable=True))
    op.add_column('Employees_logins', sa.Column('Emp_Logged_Out', sa.DateTime(), nullable=True))
    op.add_column('Issue_Timeline', sa.Column('Ist_Created', sa.DateTime(), nullable=True))


def downgrade():
    pass
