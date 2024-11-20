import itertools
import string
import copy

def delete_subset_chars(string, subset):
    """Deletes all characters from 'string' that are present in 'subset'."""

    result = ""
    for char in string:
        
        if char not in subset:
            result += char

    return result

def find_if_nullable(string,stack):
	result = ""
	for char in string:
		if char not in stack:
			result += char
	
	if result == "":
		return True
	else:
		return False

def is_name_used_in_data(grammar, name):
    # Collect all 'data' values into a single set
    all_data = {item for rule in grammar for item in rule['data']}
    # Check if 'name' is in the collected data
    return name in all_data


case1_grammar = []
case1_grammar.append(dict(name='S',data=['A','B']))
case1_grammar.append(dict(name='A',data=['L']))
case1_grammar.append(dict(name='B',data=['aBb','b']))

case2_grammar = []
case2_grammar.append(dict(name='S',data=['a','aA','B','C']))
case2_grammar.append(dict(name='A',data=['aB','L']))
case2_grammar.append(dict(name='B',data=['Aa']))
case2_grammar.append(dict(name='C',data=['cCD']))
case2_grammar.append(dict(name='D',data=['ddd','Cd']))

e3_grammar = []
e3_grammar.append(dict(name='S',data=['ABaC']))
e3_grammar.append(dict(name='A',data=['BC']))
e3_grammar.append(dict(name='B',data=['b','L']))
e3_grammar.append(dict(name='C',data=['D','L']))
e3_grammar.append(dict(name='D',data=['d']))

ex6_6_grammar = []
ex6_6_grammar.append(dict(name='S',data=['Aa','B']))
ex6_6_grammar.append(dict(name='B',data=['A','bb']))
ex6_6_grammar.append(dict(name='A',data=['a','bc','B']))

e5_grammar = []
e5_grammar.append(dict(name='S',data=['aA']))
e5_grammar.append(dict(name='A',data=['BB']))
e5_grammar.append(dict(name='B',data=['aBb','L']))

ex6_2_grammar = []
ex6_2_grammar.append(dict(name='S',data=['A']))
ex6_2_grammar.append(dict(name='A',data=['aA','L']))
ex6_2_grammar.append(dict(name='B',data=['bA']))

ex6_3_grammar = []
ex6_3_grammar.append(dict(name='S',data=['aS','A','C']))
ex6_3_grammar.append(dict(name='A',data=['a']))
ex6_3_grammar.append(dict(name='B',data=['aa']))
ex6_3_grammar.append(dict(name='C',data=['aCb']))


def theorem_6_5(g):
	grammar = copy.deepcopy(g)



	# necessary to keep track of original data for removing unit prods
	grammar_dict = {rule['name']: rule['data'] for rule in grammar}

	print('ORIGINAL:')
	for entry in grammar:
		print(entry['name'], '->', entry['data'])













	# Order: Lambda, Unit, Useless

















	# Lambda substitution
	# Lambda # Lambda # Lambda # Lambda # Lambda
	# 1. for all productions A->L put A into Vn
	# push into stack if contains lambda
	stack = []
	empty_stack = []
	for entry in grammar:
		data = entry['data']
		if 'L' in data:
			stack.append(entry['name'])
			entry['data'].remove('L')
			if entry['data'] == []:
				empty_stack.append(entry['name'])


	while(True):
		isUpdated = False
		# print(empty_stack,'empty stack')
		for item in empty_stack:
			for i, entry in enumerate(grammar):
				if entry['data'] == []:
					isUpdated = True
					grammar.pop(i)
				for j, production in enumerate(entry['data']):
					# print(item, production)
					if item in production:
						isUpdated = True
						entry['data'].remove(production)
		if isUpdated == False:
			break
		else:
			isUpdated = False




	new_entry = False
	count = 0
	while(True):
		for entry in grammar:
			for production in entry['data']:
				if find_if_nullable(production,stack):
					if entry['name'] not in stack:
						stack.append(entry['name'])
						new_entry = True
		if new_entry == False:
			break
		else:
			new_entry = False

	# print('stack:',stack)

	for L in range(len(stack) + 1):
	    for subset in itertools.combinations(stack, L):
	        for i, entry in enumerate(grammar):
	        	for string in entry['data']:
		        	result = delete_subset_chars(string,subset)
		        	if result in entry['data'] or result == '':
		        		continue
		        	else:
		        		entry['data'].append(result)
	print("LAMBDA:")
	for entry in grammar:
		print(entry['name'], '->', entry['data'])













	# Unit 
	# Unit # Unit # Unit # Unit # Unit # Unit 

	new_entry = False
	while(True):
		for i,entry in enumerate(grammar):
			stack = []
			for j,production in enumerate(entry['data']):
				#check to see if unit production
				if len(production) == 1:
					if production.isupper():
						
						# A->A delete without concequence
						if entry['name'] == production:
							entry['data'].remove(production)
						# A->B
						else:
							new_entry = True
							entry['data'].remove(production)
							curr_ = entry['data']
							for k in grammar_dict[production]:
								if k not in entry['data']:
									entry['data'].append(k)
							
		if new_entry == False:
			break
		else:
			new_entry = False




	print("UNARY:")
	for entry in grammar:
		print(entry['name'], '->', entry['data'])













	# USELESS
	# USELESS # USELESS # USELESS # USELESS # USELESS





	while(True):
		isUpdated = False
		reachable_stack = []
		reachable_stack.append('S')
		for i, entry in enumerate(grammar):
			hasTerminal = False
			stack = []
			if entry['name'] not in reachable_stack:
				isUpdated = True
				# print(entry['name'])
				grammar.pop(i)

				
			for j, production in enumerate(entry['data']):

				if production.islower():
					hasTerminal = True
					# print(production,'true')
				else:
					for char in production:
						if char.isupper():
							if char not in stack:
								stack.append(char)
							if char not in reachable_stack:
								reachable_stack.append(char)
								# print('reach:',reachable_stack)
					# print(production,'false')

			if hasTerminal:
				continue
			else:
				for item in stack:
					if entry['name'] == item:
						entry['data'].remove(production)
						isUpdated = True
		if isUpdated == False:
			break
		else:
			isUpdated = False



		stack = []
		for i, entry in enumerate(grammar):
			if entry['data'] == []:
				stack.append(entry['name'])
				# print(entry['name'])

		while(True):
			for item in stack:
				for i, entry in enumerate(grammar):
					if entry['data'] == []:
						isUpdated = True
						grammar.pop(i)
					for j, production in enumerate(entry['data']):
						if item in production:
							isUpdated = True
							entry['data'].remove(production)
			if isUpdated == False:
				break
			else:
				isUpdated = False


	print("USELESS:")
	for entry in grammar:
		print(entry['name'], '->', entry['data'])

	return grammar
###




def find_name_by_data(grammar, data_value):
    for rule in grammar:
        if data_value in rule['data']:
            return rule['name']  # Return the first match
    return None  # If not found

def get_data_by_name(grammar, name):
    for rule in grammar:
        if rule['name'] == name:
            return rule['data']  # Return the 'data' list
    return None  # Return None if name not found



## CHOMSKY
# CHOMSKYCHOMSKYCHOMSKYCHOMSKY
def chomsky(g):
	grammar = copy.deepcopy(g)
	alpha = []
	terminal = []
	
	for i,entry in enumerate(grammar):
		if entry['name'] not in alpha:
			alpha.append(entry['name'])
			# print('alpha',alpha)
		# print('...')
		for j, data in enumerate(entry['data']):
			if data.isupper():
				continue
			if len(data) == 1 and data.islower():
				if data not in terminal:
					terminal.append(data)
					# print('terminal',terminal)
				continue

	while(True):
		grammar_dict = {rule['name']: rule['data'] for rule in grammar}

		isUpdated = False
		for i,entry in enumerate(grammar):
			for j, data in enumerate(entry['data']):
				# print(data)
				for char in data:
					if char.isupper():
						continue
					else:
						t = char
						if t not in terminal:
							for letter in string.ascii_uppercase:
								if letter not in alpha:
									grammar.append(dict(name=letter,data=[t]))
									terminal.append(t)
									isUpdated = True
									break
		# replace all new terminals with variables
		for i,entry in enumerate(grammar):
			for j, data in enumerate(entry['data']):
				if len(data) == 1 and data.islower():
					continue
				if data.isupper():
					continue
				# print('data',data)

				temp = ''
				
				
				for k,char in enumerate(data):
					if char.islower():
						variable = find_name_by_data(grammar,char)
						# print(char,variable)
						temp += variable
					else:
						temp += char
				entry['data'][j] = temp

		if isUpdated == False:
			break
		else:
			isUpdated = False

	print("CHOMSKY:")
	for entry in grammar:
		print(entry['name'], '->', entry['data'])
				

## GREIBACH
def greibach(g):
	grammar = copy.deepcopy(g)
	alpha = []
	terminal = []


	for i,entry in enumerate(grammar):
		if entry['name'] not in alpha:
			alpha.append(entry['name'])
			# print('alpha',alpha)
		# print('...')
	for i,entry in enumerate(grammar):
		for j, data in enumerate(entry['data']):
			if data.isupper():
				continue
			if len(data) == 1 and data.islower():
				if data not in terminal:
					terminal.append(data)
					# print('terminal',terminal)
				continue
	while(True):
		isUpdated = False

		for i,entry in enumerate(grammar):
			for j,data in enumerate(entry['data']):
				for k, char in enumerate(data):
					if k == 0:
						if char.isupper():

							productions = get_data_by_name(grammar,char)
							prev = data[1:]
							entry['data'].remove(data)
							isUpdated = True
							count = 0
							for p in productions:
								count += 1
								if (p+prev) not in entry['data']:
									entry['data'].append(p+prev)
								
							# print('islower',char)
						continue
		if isUpdated == False:
			break
		else:
			isUpdated = False



	for i,entry in enumerate(grammar):
			for j,data in enumerate(entry['data']):
				temp = ''
				for k, char in enumerate(data):
					if k == 0:
						temp += char
						continue
					if char.isupper():
						temp += char
						continue
					t = char
					if t not in terminal:
						print('yo......')
						for letter in string.ascii_uppercase:
							if letter not in alpha:
								grammar.append(dict(name=letter,data=[t]))
								terminal.append(t)
								temp+=letter
								break
					else:
						variable = find_name_by_data(grammar,t)
						temp += variable
				entry['data'][j] = temp





	#print('terminal',terminal)
	#print('alpha',alpha)


	print("GREIBACH:")
	for entry in grammar:
		print(entry['name'], '->', entry['data'])











print('\tTest Case 1:')
g = theorem_6_5(case1_grammar)
chomsky(g)
greibach(g)

print('\n\n\tTest Case 2:')
g = theorem_6_5(case2_grammar)
chomsky(g)
greibach(g)
