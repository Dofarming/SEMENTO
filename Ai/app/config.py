from dotenv import load_dotenv
import os
import pandas as pd
import numpy as np

# .env 파일 로드
load_dotenv()

API_KEY = os.getenv("API_KEY")
HOST = os.getenv("HOST")
PORT = int(os.getenv("PORT"))

THRESHOLD = 0.276245

N_OHTS = 30
FAILURE_DEADLINE = 200
CONGESTION_CRITERION_TIME = 20

path_and_before_path_info = pd.read_excel("./xlsx/paths.xlsx")
node_path_info = pd.read_csv("./xlsx/path_link_info.csv")
facility_length_info_excel = pd.read_excel("./xlsx/facility_node_path_length.xlsx")

# node 컬럼 생성
path_and_before_path_info['before_node'] = ''
# path_info와 path_and_before_path_info 'path' 열을 기준으로 병합하여 node 열에 end_node 값 추가
path_and_before_path_info['before_node'] = path_and_before_path_info.apply(lambda row: node_path_info.loc[node_path_info['path'] == row['path'], 'end_node'].iloc[0] if row['path'] in node_path_info['path'].values else '', axis=1)


length_info = {}
facility_length_info = {}

for row in path_and_before_path_info.iterrows():
    length_info[row[1]['path']] = row[1]['length']
    
for row in facility_length_info_excel.iterrows():
    facility_length_info[row[1]['path']] = row[1]['length']

all_nodes = np.array([np.nan, 'node281', 'node276', 'node119', 'node162', 'node235',
       'node99', 'node203', 'node145', 'node240', 'node38', 'node297',
       'node311', 'node94', 'node194', 'node87', 'node178', 'node140',
       'node153', 'node104', 'node170', 'node114', 'node208', 'node6',
       'node72', 'node127', 'node136', 'node157', 'node189', 'node213',
       'node165', 'node50', 'node28', 'node77', 'node225', 'node256',
       'node308', 'node16', 'node267', 'node21', 'node247', 'node288',
       'node186', 'node43', 'node65', 'node82', 'node230', 'node131',
       'node60', 'transportNode', 'node1', 'node2', 'node11', 'node10',
       'node12', 'node20', 'node23', 'node24', 'node22', 'node33',
       'node32', 'node34', 'node42', 'node44', 'node46', 'node55',
       'node45', 'node54', 'node56', 'node57', 'node58', 'node59',
       'node64', 'node66', 'node129', 'node130', 'node61', 'node62',
       'node63', 'node67', 'node68', 'node76', 'node132', 'node78',
       'node133', 'node86', 'node176', 'node177', 'node88', 'node121',
       'node89', 'node179', 'node90', 'node122', 'node98', 'node123',
       'node242', 'node100', 'node126', 'node243', 'node109', 'node108',
       'node307', 'node251', 'node110', 'node252', 'node111', 'node112',
       'node260', 'node306', 'node262', 'node263', 'node196', 'node309',
       'node197', 'node198', 'node271', 'node272', 'node199', 'node273',
       'node274', 'node275', 'node261', 'node207', 'node209', 'node135',
       'node218', 'node217', 'node137', 'node219', 'node138', 'node139',
       'node200', 'node188', 'node141', 'node201', 'node144', 'node202',
       'node146', 'node190', 'node147', 'node142', 'node191', 'node148',
       'node168', 'node277', 'node117', 'node169', 'node278', 'node118',
       'node149', 'node150', 'node279', 'node280', 'node160', 'node172',
       'node282', 'node161', 'node124', 'node173', 'node184', 'node174',
       'node164', 'node185', 'node204', 'node166', 'node205', 'node206',
       'node187', 'node171', 'node220', 'node221', 'node229', 'node253',
       'node231', 'node254', 'node232', 'node180', 'node233', 'node255',
       'node181', 'node234', 'node182', 'node239', 'node193', 'node195',
       'node163', 'node283', 'node257', 'node284', 'node258', 'node310',
       'node259', 'node292', 'node293', 'node120', 'node236', 'node237',
       'node91', 'node238', 'node92', 'node241', 'node93', 'node264',
       'node265', 'node266', 'node35', 'node95', 'node36', 'node96',
       'node97', 'node37', 'node268', 'node269', 'node47', 'node270',
       'node48', 'node49', 'node183', 'node301', 'node302', 'node125',
       'node303', 'node304', 'node156', 'node39', 'node128', 'node25',
       'node26', 'node40', 'node158', 'node27', 'node41', 'node294',
       'node295', 'node296', 'node51', 'node52', 'node53', 'node29',
       'node30', 'node31', 'node101', 'node102', 'node103', 'node210',
       'node211', 'node212', 'node113', 'node285', 'node286', 'node287',
       'node175', 'node134', 'node244', 'node159', 'node245', 'node246',
       'node151', 'node105', 'node152', 'node106', 'node107', 'node214',
       'node154', 'node215', 'node155', 'node216', 'node289', 'node290',
       'node3', 'node291', 'node4', 'node69', 'node5', 'node70', 'node79',
       'node71', 'node80', 'node81', 'node248', 'node249', 'node250',
       'node115', 'node83', 'node84', 'node85', 'node7', 'node8', 'node9',
       'node222', 'node223', 'node224', 'node73', 'node74', 'node75',
       'node298', 'node299', 'node300', 'node226', 'node227', 'node228',
       'node305', 'node13', 'node14', 'node15', 'node17', 'node18',
       'node19', 'node167', 'node143', 'node116', 'node192'])

all_paths = np.array([np.nan, 'path', 'path1', 'path8', 'path309', 'path16', 'path15',
       'path316', 'path32', 'path82', 'path24', 'path81', 'path317',
       'path17', 'path25', 'path318', 'path40', 'path83', 'path33',
       'path319', 'path84', 'path34', 'path42', 'path186', 'path41',
       'path35', 'path39', 'path320', 'path48', 'path104', 'path105',
       'path339', 'path38', 'path37', 'path36', 'path185', 'path85',
       'path86', 'path360', 'path49', 'path321', 'path50', 'path151',
       'path51', 'path152', 'path322', 'path64', 'path153', 'path155',
       'path98', 'path340', 'path87', 'path99', 'path88', 'path355',
       'path63', 'path333', 'path323', 'path65', 'path100', 'path241',
       'path66', 'path101', 'path250', 'path324', 'path336', 'path89',
       'path338', 'path251', 'path90', 'path260', 'path91', 'path341',
       'path78', 'path283', 'path328', 'path263', 'path272', 'path177',
       'path344', 'path178', 'path79', 'path273', 'path274', 'path207',
       'path275', 'path276', 'path277', 'path327', 'path262', 'path217',
       'path216', 'path106', 'path107', 'path282', 'path329', 'path359',
       'path218', 'path342', 'path358', 'path109', 'path199', 'path111',
       'path110', 'path346', 'path200', 'path167', 'path112', 'path201',
       'path113', 'path202', 'path349', 'path331', 'path115', 'path278',
       'path168', 'path117', 'path142', 'path169', 'path315', 'path143',
       'path279', 'path170', 'path145', 'path280', 'path95', 'path129',
       'path130', 'path281', 'path345', 'path305', 'path131', 'path161',
       'path147', 'path352', 'path135', 'path162', 'path348', 'path343',
       'path163', 'path154', 'path137', 'path203', 'path165', 'path350',
       'path204', 'path332', 'path144', 'path205', 'path206', 'path357',
       'path146', 'path239', 'path228', 'path219', 'path334', 'path252',
       'path229', 'path253', 'path114', 'path230', 'path254', 'path156',
       'path231', 'path157', 'path232', 'path255', 'path337', 'path238',
       'path233', 'path164', 'path335', 'path261', 'path174', 'path175',
       'path314', 'path326', 'path176', 'path351', 'path256', 'path285',
       'path257', 'path294', 'path141', 'path258', 'path330', 'path347',
       'path259', 'path295', 'path304', 'path354', 'path96', 'path234',
       'path235', 'path198', 'path236', 'path197', 'path237', 'path77',
       'path240', 'path76', 'path353', 'path264', 'path166', 'path265',
       'path266', 'path267', 'path75', 'path31', 'path184', 'path74',
       'path30', 'path73', 'path196', 'path29', 'path356', 'path268',
       'path269', 'path80', 'path270', 'path188', 'path271', 'path47',
       'path312', 'path158', 'path46', 'path159', 'path306', 'path307',
       'path160', 'path308', 'path28', 'path124', 'path125', 'path23',
       'path27', 'path103', 'path284', 'path181', 'path311', 'path22',
       'path26', 'path132', 'path21', 'path183', 'path296', 'path297',
       'path298', 'path45', 'path116', 'path299', 'path44', 'path136',
       'path43', 'path187', 'path20', 'path19', 'path18', 'path182',
       'path72', 'path195', 'path71', 'path70', 'path208', 'path209',
       'path210', 'path211', 'path92', 'path93', 'path286', 'path287',
       'path288', 'path289', 'path149', 'path150', 'path242', 'path126',
       'path243', 'path127', 'path244', 'path108', 'path245', 'path69',
       'path128', 'path68', 'path119', 'path212', 'path67', 'path102',
       'path194', 'path310', 'path213', 'path120', 'path214', 'path122',
       'path215', 'path290', 'path291', 'path2', 'path292', 'path133',
       'path293', 'path193', 'path3', 'path192', 'path4', 'path57',
       'path62', 'path190', 'path61', 'path56', 'path55', 'path246',
       'path247', 'path248', 'path249', 'path325', 'path94', 'path148',
       'path54', 'path53', 'path52', 'path5', 'path189', 'path6', 'path7',
       'path134', 'path118', 'path220', 'path221', 'path60', 'path222',
       'path223', 'path59', 'path58', 'path191', 'path300', 'path301',
       'path97', 'path302', 'path303', 'path224', 'path225', 'path226',
       'path227', 'path123', 'path313', 'path14', 'path179', 'path13',
       'path12', 'path11', 'path10', 'path9', 'path180', 'path139',
       'path140', 'path138', 'path171', 'path172', 'path173'])


df_cols = ['oht_id', 'mode', 'status', 'current_node', 'next_node',
       'target_node', 'carrier', 'error', 'oht_connect', 'curr_node_offset', 'speed',
       'is_fail', 'curr_time', 'start_time', 'point_x', 'point_y', 'path']