"""
Pyicoteolib is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
"""

from __future__ import with_statement
from distutils.core import setup
from pyicoteolib.defaults import VERSION


setup(name='Pyicoteo',
      version=str(VERSION),
      description='Pyicoteo: HTS genomics coordintes analysis suite.',
      author=u'Juan Gonzalez_Vallinas',
      author_email='juanramon.gonzalezvallinas@upf.edu',
      url='http://regulatorygenomics.upf.edu/pyicoteo',
      packages = ['pyicoteolib.parser', 'pyicoteolib.chromlen'],
      package_data={
          'pyicoteolib.chromlen': [
              '*'
          ],
      },
      scripts = ['pyicos', 'pyicotrocol', 'pyicoller', 'pyicoenrich', 'pyicoclip', 'pyicoregion'],
      py_modules = ['pyicoteolib.core', 'pyicoteolib.turbomix','pyicoteolib.utils',
                    'pyicoteolib.parser.utils', 'pyicoteolib.parser.pyicoclip', 'pyicoteolib.parser.pyicoenrich',
                    'pyicoteolib.parser.pyicoregion', 'pyicoteolib.defaults', 'pyicoteolib.bam', 
                    'pyicoteolib.enrichment', 'pyicoteolib.regions']
     )

