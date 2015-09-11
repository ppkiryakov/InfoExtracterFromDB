select e.first_name, 
       e.last_name, 
       dep.dept_name "department" 
from employees e 
inner join dept_emp  
        on dept_emp.emp_no = e.emp_no 
inner join departments dep  
		on dep.dept_no = dept_emp.dept_no ;
