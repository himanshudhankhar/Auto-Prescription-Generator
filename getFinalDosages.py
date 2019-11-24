from dosages import getDosages
import sys
import json
arguments=sys.argv
diseasesSelected=[]
for i in range(1,len(arguments)):
    diseasesSelected.append(arguments[i])
dosages = getDosages(diseasesSelected)
answer={
    "dosages":dosages
}
outputFinal  = json.dumps(answer)
print(outputFinal)
