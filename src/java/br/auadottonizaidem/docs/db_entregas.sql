-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Servidor: 127.0.0.1
-- Tempo de Geração: 
-- Versão do Servidor: 5.5.27
-- Versão do PHP: 5.4.7

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Banco de Dados: `db_entregas`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `empresa`
--

CREATE TABLE IF NOT EXISTS `empresa` (
  `id_empresa` int(11) NOT NULL AUTO_INCREMENT,
  `nome_empresa` varchar(100) NOT NULL,
  `logradouro` varchar(100) DEFAULT NULL,
  `numero` int(11) DEFAULT NULL,
  `bairro` varchar(45) DEFAULT NULL,
  `estado` varchar(45) DEFAULT NULL,
  `telefone` varchar(11) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `user` varchar(45) DEFAULT NULL,
  `senha` varchar(40) DEFAULT NULL,
  `preco_moto` double DEFAULT NULL,
  `preco_carro` double DEFAULT NULL,
  `preco_caminhao` double DEFAULT NULL,
  PRIMARY KEY (`id_empresa`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estrutura da tabela `ta_rota`
--

CREATE TABLE IF NOT EXISTS `ta_rota` (
  `id_rota` int(11) NOT NULL,
  `loc_destino` int(11) NOT NULL,
  `loc_origem` int(11) NOT NULL,
  PRIMARY KEY (`id_rota`),
  KEY `loc_destino` (`loc_destino`),
  KEY `loc_origem` (`loc_origem`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `ta_rota_percurso`
--

CREATE TABLE IF NOT EXISTS `ta_rota_percurso` (
  `id_rota` int(11) NOT NULL AUTO_INCREMENT,
  `id_ponto_referencia` int(11) NOT NULL DEFAULT '0',
  `sequencia` int(11) DEFAULT NULL,
  `distancia` double DEFAULT NULL,
  PRIMARY KEY (`id_rota`,`id_ponto_referencia`),
  KEY `id_ponto_referencia` (`id_ponto_referencia`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estrutura da tabela `ta_status`
--

CREATE TABLE IF NOT EXISTS `ta_status` (
  `id_rota` int(11) NOT NULL DEFAULT '0',
  `id_rota_percurso` int(11) NOT NULL DEFAULT '0',
  `id_entrega` int(11) NOT NULL DEFAULT '0',
  `data_hora_passagem_ponto` datetime DEFAULT NULL,
  PRIMARY KEY (`id_rota`,`id_rota_percurso`,`id_entrega`),
  KEY `id_entrega` (`id_entrega`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `tb_cliente`
--

CREATE TABLE IF NOT EXISTS `tb_cliente` (
  `id_cliente` int(11) NOT NULL AUTO_INCREMENT,
  `nome_usuario` varchar(100) NOT NULL,
  `nacionalidade` varchar(45) DEFAULT NULL,
  `est_civil` char(1) DEFAULT NULL,
  `idade` int(3) DEFAULT NULL,
  `logradouro` varchar(100) DEFAULT NULL,
  `numero` int(11) DEFAULT NULL,
  `bairro` varchar(45) DEFAULT NULL,
  `estado` varchar(45) DEFAULT NULL,
  `telefone` varchar(11) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `user` varchar(45) NOT NULL,
  `senha` varchar(40) NOT NULL,
  PRIMARY KEY (`id_cliente`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estrutura da tabela `tb_entrega`
--

CREATE TABLE IF NOT EXISTS `tb_entrega` (
  `id_entrega` int(11) NOT NULL AUTO_INCREMENT,
  `id_cliente` int(11) DEFAULT NULL,
  `id_rota` int(11) DEFAULT NULL,
  `placa_veiculo` varchar(7) DEFAULT NULL,
  `descricao` varchar(100) DEFAULT NULL,
  `valor` double DEFAULT NULL,
  `peso` double DEFAULT NULL,
  PRIMARY KEY (`id_entrega`),
  KEY `id_cliente` (`id_cliente`),
  KEY `id_rota` (`id_rota`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estrutura da tabela `tb_localidade`
--

CREATE TABLE IF NOT EXISTS `tb_localidade` (
  `id_localizacao` int(11) NOT NULL AUTO_INCREMENT,
  `regiao` varchar(45) NOT NULL,
  PRIMARY KEY (`id_localizacao`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estrutura da tabela `tb_ponto_referencia`
--

CREATE TABLE IF NOT EXISTS `tb_ponto_referencia` (
  `id_ponto_referencia` int(11) NOT NULL AUTO_INCREMENT,
  `descr_ponto` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_ponto_referencia`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estrutura da tabela `tb_veiculo`
--

CREATE TABLE IF NOT EXISTS `tb_veiculo` (
  `placa_veiculo` int(11) NOT NULL AUTO_INCREMENT,
  `nome_motorista` varchar(100) DEFAULT NULL,
  `tipo_veiculo` int(1) DEFAULT NULL,
  PRIMARY KEY (`placa_veiculo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Restrições para as tabelas dumpadas
--

--
-- Restrições para a tabela `ta_rota`
--
ALTER TABLE `ta_rota`
  ADD CONSTRAINT `ta_rota_ibfk_1` FOREIGN KEY (`loc_destino`) REFERENCES `tb_localidade` (`id_localizacao`),
  ADD CONSTRAINT `ta_rota_ibfk_2` FOREIGN KEY (`loc_origem`) REFERENCES `tb_localidade` (`id_localizacao`);

--
-- Restrições para a tabela `ta_rota_percurso`
--
ALTER TABLE `ta_rota_percurso`
  ADD CONSTRAINT `ta_rota_percurso_ibfk_2` FOREIGN KEY (`id_ponto_referencia`) REFERENCES `tb_ponto_referencia` (`id_ponto_referencia`),
  ADD CONSTRAINT `ta_rota_percurso_ibfk_1` FOREIGN KEY (`id_rota`) REFERENCES `ta_rota` (`id_rota`);

--
-- Restrições para a tabela `ta_status`
--
ALTER TABLE `ta_status`
  ADD CONSTRAINT `ta_status_ibfk_2` FOREIGN KEY (`id_entrega`) REFERENCES `tb_entrega` (`id_entrega`),
  ADD CONSTRAINT `ta_status_ibfk_1` FOREIGN KEY (`id_rota`, `id_rota_percurso`) REFERENCES `ta_rota_percurso` (`id_rota`, `id_ponto_referencia`);

--
-- Restrições para a tabela `tb_entrega`
--
ALTER TABLE `tb_entrega`
  ADD CONSTRAINT `tb_entrega_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `tb_cliente` (`id_cliente`),
  ADD CONSTRAINT `tb_entrega_ibfk_2` FOREIGN KEY (`id_rota`) REFERENCES `ta_rota` (`id_rota`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
