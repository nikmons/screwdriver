"""empty message

Revision ID: 27c544cc6a24
Revises: 865a544d9558
Create Date: 2018-12-23 20:14:53.235355

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '27c544cc6a24'
down_revision = '865a544d9558'
branch_labels = None
depends_on = None


def upgrade():
    op.drop_column('Issues', 'Issue_Created')
    op.drop_column('Issues', 'Issue_Closed')
    op.add_column('Issues', sa.Column('Issue_Created', sa.DateTime(), nullable=True))
    op.add_column('Issues', sa.Column('Issue_Closed', sa.DateTime(), nullable=True))
    pass


def downgrade():
    pass
