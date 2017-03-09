#script for parsing the files of puzzles into individual files and associated solutions
dif = ["simple" , "easy" , "intermediate" , "expert"]
for n in range(len(dif)):
	i = open(dif[n] + '/' + dif[n] + '.txt', 'r')
	files = list(range(1,21))
	for counter in files:
		puz = open(dif[n] + '/' + dif[n] + 'Puz' + str(counter)+'.txt','w')
		sol = open(dif[n] + '/' + dif[n] + 'Sol' + str(counter)+'.txt','w')
		for line in range(0,9):
			puz.write(i.readline())
		i.readline()
		for line in range(0,9):
			sol.write(i.readline())
		i.readline()