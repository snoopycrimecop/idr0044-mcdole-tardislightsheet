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

splitchar = ","

##########

datasets = set()
channelNames = {}
df = pandas.read_csv(annoFile)
for index, row in df.iterrows():
    datasets.add(row["Dataset Name"])
    channelNames[row["Image Name"]] = row["Channels"]

conn = BlitzGateway(user, password, host=host)
conn.connect()

project = conn.getObject("Project", projectId)
for ds in project.listChildren():
    if ds.name in datasets:
        for img in ds.listChildren():

            channels = img.getChannels(noRE=True)

            if img.getName() not in channelNames:
                print "No channel names found for %s" % img.getName()
                continue

            names = channelNames[img.getName()].split(splitchar)
            if len(names) == 0:
                print "No channel names found for %s" % img.getName()
                continue

            if len(channels) != len(names):
                print "Number of channel names (%d) doesn't match number of channels (%d) (%s)" % (len(channels), len(names), img.getName())
                continue

            for i, ch in enumerate(channels):
                lc = ch.getLogicalChannel()
                lc.setName(names[i])
                lc.save()

            print "Updated channel names for %s" % img.getName()

