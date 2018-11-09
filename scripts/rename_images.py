from omero.gateway import BlitzGateway
import pandas

'''
This script renames the images from 'Imported Image Name'
to 'Image Name' specified in the annotation.csv.
'''

# Files
annoFile = "../experimentA/idr0044-experimentA-annotation.csv"

# OMERO credentials
user = "root"
password = "xx"
host = "localhost"

projectId = 1307

##########

datasets = set()
imageNames = {}
df = pandas.read_csv(annoFile)
for index, row in df.iterrows():
    datasets.add(row["Dataset Name"])
    imageNames[row["Imported Image Name"]] = row["Image Name"]

conn = BlitzGateway(user, password, host=host)
conn.connect()

project = conn.getObject("Project", projectId)
for ds in project.listChildren():
    if ds.name in datasets:
        for img in ds.listChildren():
            if img.getName() in imageNames:
                oldName = img.getName()
                img.setName(imageNames[oldName])
                img.save()
                print "Renamed %s to %s" % (oldName, imageNames[oldName])
