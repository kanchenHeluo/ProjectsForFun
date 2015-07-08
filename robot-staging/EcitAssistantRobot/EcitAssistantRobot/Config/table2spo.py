import sys

def main():
    first_line = True
    field_name_list = []
    while 1:
        line = sys.stdin.readline()
        if line == '':
            break
        arr = line.strip().split('\t')
        if first_line:
            field_name_list = arr
            first_line = False
        else:
            item_name = arr[0]
            for idx, data in enumerate(arr):
                if data == '':
                    continue
                print '%s\t%s\t%s' % (item_name, field_name_list[idx], data)




if __name__ == '__main__':
    main()