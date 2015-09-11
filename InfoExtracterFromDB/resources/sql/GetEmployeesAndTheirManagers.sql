select e.first_name,   
       e.last_name,   
       dep.dept_name "department",   
       concat( managers.name, ' ' , managers.lastname) as "manager"  
from employees e 
inner join dept_emp  
        on dept_emp.emp_no = e.emp_no 
inner join departments dep  
		on dep.dept_no = dept_emp.dept_no 
inner join (
   select e.first_name name , 
	   e.last_name as lastname, 
       dep.dept_name as "department", 
       dep.dept_no as dept_no, 
	   mngr.emp_no as mngr_no 
	from employees e 
	inner join dept_manager mngr  
        	on mngr.emp_no = e.emp_no 
	inner join dept_emp   
			on dept_emp.emp_no = e.emp_no 
	inner join departments dep  
			on dep.dept_no = dept_emp.dept_no  ) as managers  
		on managers.dept_no = dept_emp.dept_no  
order by e.first_name ;   
