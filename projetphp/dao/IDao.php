<?php 
interface IDao {
    public function create($o);
    public function update($o);
    public function delete($o);
    public function findAll();
    public function findById($id);

}


?>