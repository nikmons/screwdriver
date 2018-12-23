"""empty message

Revision ID: 865a544d9558
Revises: e6047eb3a998
Create Date: 2018-12-23 19:54:11.534857

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '865a544d9558'
down_revision = 'e6047eb3a998'
branch_labels = None
depends_on = None


def upgrade():
    op.drop_column('Customers', 'Cust_Created')
    op.drop_column('Customers', 'Cust_Birth_Date')
    op.add_column('Customers', sa.Column('Cust_Created', sa.DateTime(), nullable=True))
    op.add_column('Customers', sa.Column('Cust_Birth_Date', sa.DateTime(), nullable=True))
    pass


def downgrade():
    pass
